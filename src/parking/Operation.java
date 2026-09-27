package parking;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Operation class that is the user interface of the Parking Management
 * System.
 * It reads the command lines entered on the terminal, processes each
 * command, and displays the results until the Q command is entered.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class Operation {
    /** The value returned when a token is not a valid number. */
    private static final int NOT_A_NUMBER = -1;
    /** The number of tokens after the deck number to open a deck. */
    private static final int OPEN_TOKENS = 3;
    /** The number of data tokens of the E command. */
    private static final int ENTER_TOKENS = 4;
    /** The number of data tokens of the X command. */
    private static final int EXIT_TOKENS = 3;
    /** The number of parts in a time string hh:mm. */
    private static final int TIME_PARTS = 2;
    /** The largest valid hour in a 24-hour time format. */
    private static final int MAX_HOUR = 23;
    /** The largest valid minute. */
    private static final int MAX_MINUTE = 59;
    /** The number of minutes in an hour. */
    private static final int MINUTES_PER_HOUR = 60;
    /** The number of hours in a day. */
    private static final int HOURS_PER_DAY = 24;
    /** The maximum number of days a vehicle can stay in a deck. */
    private static final int MAX_DAYS_PARKED = 2;
    /** The maximum number of minutes a vehicle can stay. */
    private static final long MAX_MINUTES_PARKED =
            MAX_DAYS_PARKED * HOURS_PER_DAY * MINUTES_PER_HOUR;
    /** The delimiter between the hour and the minute. */
    private static final String TIME_DELIMITER = ":";

    /** The list of registered vehicles. */
    private VehicleList vehicleList;
    /** The list of parking decks. */
    private DeckList deckList;

    /**
     * Create the user interface with an empty vehicle list and deck list.
     */
    public Operation() {
        vehicleList = new VehicleList();
        deckList = new DeckList();
    }

    /**
     * Process a single command with its data tokens.
     * @param command the command at the beginning of the command line.
     * @param tokens the data tokens following the command.
     */
    private void processCommand(String command, StringTokenizer tokens) {
        switch (command) {
            case "A":
                addVehicle(tokens);
                break;
            case "R":
                removeVehicle(tokens);
                break;
            case "O":
                openDeck(tokens);
                break;
            case "C":
                closeDeck(tokens);
                break;
            case "E":
                enterDeck(tokens);
                break;
            case "X":
                exitDeck(tokens);
                break;
            case "PP":
                vehicleList.printByPlate();
                break;
            case "PD":
                printDecks(tokens);
                break;
            case "PH":
                printHistory(tokens);
                break;
            default:
                System.out.println(command + " is an invalid command!");
        }
    }

    /**
     * Check if the command line has enough data tokens left.
     * An error message is displayed if tokens are missing.
     * @param tokens the data tokens of the command line.
     * @param count the number of tokens required.
     * @return true if there are enough tokens; false otherwise.
     */
    private boolean hasTokens(StringTokenizer tokens, int count) {
        if (tokens.countTokens() < count) {
            System.out.println("Error - missing data tokens.");
            return false;
        }
        return true;
    }

    /**
     * Check if a license plate is in a valid format.
     * An error message is displayed if the plate is not valid.
     * @param vehicle the vehicle holding the license plate.
     * @param plate the license plate as entered by the user.
     * @return true if the plate is valid; false otherwise.
     */
    private boolean isValidPlate(Vehicle vehicle, String plate) {
        if (!vehicle.isValid()) {
            System.out.println(plate + " - invalid license plate format.");
            return false;
        }
        return true;
    }

    /**
     * Get the deck number from a token containing only digits.
     * @param token the token holding the deck number.
     * @return the deck number; NOT_A_NUMBER if the token is not valid.
     */
    private int deckNumber(String token) {
        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) < '0' || token.charAt(i) > '9') {
                return NOT_A_NUMBER;
            }
        }
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException exception) {
            return NOT_A_NUMBER;
        }
    }

    /**
     * Get the value of an hour or a minute from a token.
     * @param token the token holding the value.
     * @param maxValue the largest valid value.
     * @return the value; NOT_A_NUMBER if it is not a number from 0 to
     *         maxValue.
     */
    private int timeValue(String token, int maxValue) {
        try {
            int value = Integer.parseInt(token);
            if (value < 0 || value > maxValue) {
                return NOT_A_NUMBER;
            }
            return value;
        } catch (NumberFormatException exception) {
            return NOT_A_NUMBER;
        }
    }

    /**
     * Get the timestamp from a date token and a time token.
     * An error message is displayed if the date, hour, or minute is not
     * valid.
     * @param dateToken the date in the format yyyy-mm-dd.
     * @param timeToken the time in the format hh:mm.
     * @return the timestamp; null if the date or time is not valid.
     */
    private Timestamp timestamp(String dateToken, String timeToken) {
        Date date = new Date(dateToken);
        if (!date.isValid()) {
            System.out.println(dateToken + " - invalid calendar date.");
            return null;
        }
        String[] time = timeToken.split(TIME_DELIMITER);
        if (time.length != TIME_PARTS) {
            System.out.println(timeToken + " - invalid time format.");
            return null;
        }
        int hour = timeValue(time[0], MAX_HOUR);
        if (hour == NOT_A_NUMBER) {
            System.out.println(time[0] + " - invalid hour.");
            return null;
        }
        int minute = timeValue(time[1], MAX_MINUTE);
        if (minute == NOT_A_NUMBER) {
            System.out.println(time[1] + " - invalid minute.");
            return null;
        }
        return new Timestamp(date, hour, minute);
    }

    /**
     * Get an existing deck from a token holding the deck number.
     * An error message is displayed if the deck number is not valid or the
     * deck does not exist.
     * @param token the token holding the deck number.
     * @return the deck; null if the deck number is not valid or the deck
     *         does not exist.
     */
    private Deck existingDeck(String token) {
        int number = deckNumber(token);
        if (number == NOT_A_NUMBER) {
            System.out.println(token
                    + " - invalid deck number; it contains characters.");
            return null;
        }
        Deck deck = deckList.getDeck(new Deck(number));
        if (deck == null) {
            System.out.println("Deck#" + number + " - does not exist.");
        }
        return deck;
    }

    /**
     * Register a vehicle and add it to the vehicle list (A command).
     * @param tokens the data tokens holding the license plate.
     */
    private void addVehicle(StringTokenizer tokens) {
        if (!hasTokens(tokens, 1)) {
            return;
        }
        String plate = tokens.nextToken();
        Vehicle vehicle = new Vehicle(plate);
        if (!isValidPlate(vehicle, plate)) {
            return;
        }
        if (vehicleList.contains(vehicle)) {
            System.out.println(plate + " is already registered.");
            return;
        }
        vehicleList.add(vehicle);
        System.out.println(plate + " registered.");
    }

    /**
     * Unregister a vehicle and remove it from the vehicle list
     * (R command).
     * A vehicle parked in a deck or with a parking history is not removed.
     * @param tokens the data tokens holding the license plate.
     */
    private void removeVehicle(StringTokenizer tokens) {
        if (!hasTokens(tokens, 1)) {
            return;
        }
        String plate = tokens.nextToken();
        Vehicle vehicle = new Vehicle(plate);
        if (!isValidPlate(vehicle, plate)) {
            return;
        }
        Vehicle registered = vehicleList.getVehicle(vehicle);
        if (registered == null) {
            System.out.println(plate
                    + " does not exist; cannot be unregistered.");
        } else if (deckList.getDeckWith(registered) != null) {
            System.out.println("Cannot be unregistered - " + plate
                    + " is in a deck.");
        } else if (registered.hasHistory()) {
            System.out.println("Cannot be unregistered - " + plate
                    + " has parking history.");
        } else {
            vehicleList.remove(registered);
            System.out.println(plate + " unregistered.");
        }
    }

    /**
     * Check if the capacity token is an integer from 1 to the maximum deck
     * capacity.
     * An error message is displayed if the capacity is not valid.
     * @param token the token holding the capacity.
     * @return true if the capacity is valid; false otherwise.
     */
    private boolean isValidCapacity(String token) {
        int capacity;
        try {
            capacity = Integer.parseInt(token);
        } catch (NumberFormatException exception) {
            System.out.println(token
                    + " - invalid capacity; it's not an integer.");
            return false;
        }
        if (capacity > Deck.MAXCAPACITY) {
            System.out.println(token + " - exceeds the maximum deck "
                    + "capacity " + Deck.MAXCAPACITY);
            return false;
        }
        if (capacity < 1) {
            System.out.println(token + " - invalid capacity.");
            return false;
        }
        return true;
    }

    /**
     * Create a new deck from the location, operating hours, and capacity
     * tokens.
     * An error message is displayed if any of the data is not valid.
     * @param number the deck number.
     * @param tokens the data tokens following the deck number.
     * @return the new deck; null if any of the data is not valid.
     */
    private Deck newDeck(int number, StringTokenizer tokens) {
        String city = tokens.nextToken();
        String code = tokens.nextToken();
        String capacityToken = tokens.nextToken();
        Location location = Location.toLocation(city);
        Hour hour = Hour.toHour(code);
        if (location == null) {
            System.out.println(city + " - invalid location.");
            return null;
        }
        if (hour == null) {
            System.out.println(code + " - invalid operation hours");
            return null;
        }
        if (!isValidCapacity(capacityToken)) {
            return null;
        }
        return new Deck(number, location, hour,
                Integer.parseInt(capacityToken));
    }

    /**
     * Open a new deck or reopen a closed deck (O command).
     * @param tokens the data tokens holding the deck number, location,
     *               operating hours, and capacity.
     */
    private void openDeck(StringTokenizer tokens) {
        if (!hasTokens(tokens, 1)) {
            return;
        }
        String numberToken = tokens.nextToken();
        int number = deckNumber(numberToken);
        if (number == NOT_A_NUMBER) {
            System.out.println(numberToken + " - invalid deck number.");
            return;
        }
        Deck deck = deckList.getDeck(new Deck(number));
        if (deck != null && deck.isOpen()) {
            System.out.println("Deck#" + number + " - is already open.");
        } else if (deck != null) {
            deckList.open(deck);
            System.out.println("Deck#" + number
                    + " - was closed, now reopened.");
        } else if (tokens.countTokens() < OPEN_TOKENS) {
            System.out.println("Error opening Deck#" + number
                    + " - missing data tokens.");
        } else {
            deck = newDeck(number, tokens);
            if (deck != null) {
                deckList.open(deck);
                System.out.println("Deck#" + number + " opened.");
            }
        }
    }

    /**
     * Close an open deck that has no vehicle parked (C command).
     * @param tokens the data tokens holding the deck number.
     */
    private void closeDeck(StringTokenizer tokens) {
        if (!hasTokens(tokens, 1)) {
            return;
        }
        Deck deck = existingDeck(tokens.nextToken());
        if (deck == null) {
            return;
        }
        if (!deck.isOpen()) {
            System.out.println("Deck#" + deck.getNumber()
                    + " - is already closed.");
        } else if (deck.getNumParked() > 0) {
            System.out.println("Deck#" + deck.getNumber()
                    + " - cannot be closed; vehicles are in the deck.");
        } else {
            deckList.close(deck);
            System.out.println("Deck#" + deck.getNumber() + " - closed.");
        }
    }

    /**
     * Get a deck that is open and not full for a vehicle to enter.
     * An error message is displayed if the deck cannot be entered.
     * @param token the token holding the deck number.
     * @return the deck; null if the deck cannot be entered.
     */
    private Deck availableDeck(String token) {
        Deck deck = existingDeck(token);
        if (deck == null) {
            return null;
        }
        if (!deck.isOpen()) {
            System.out.println("Deck#" + deck.getNumber()
                    + " - is closed for parking.");
            return null;
        }
        if (deck.isFull()) {
            System.out.println("Deck#" + deck.getNumber() + " - is full.");
            return null;
        }
        return deck;
    }

    /**
     * Get a registered vehicle that is not parked in any deck.
     * An error message is displayed if the vehicle cannot enter a deck.
     * @param plate the license plate as entered by the user.
     * @return the registered vehicle; null if it cannot enter a deck.
     */
    private Vehicle parkableVehicle(String plate) {
        Vehicle vehicle = new Vehicle(plate);
        if (!isValidPlate(vehicle, plate)) {
            return null;
        }
        Vehicle registered = vehicleList.getVehicle(vehicle);
        if (registered == null) {
            System.out.println(plate + " - is not registered.");
            return null;
        }
        if (deckList.getDeckWith(registered) != null) {
            System.out.println("Error entering - " + plate
                    + " is already in a deck.");
            return null;
        }
        return registered;
    }

    /**
     * Record a parking activity when a vehicle enters a deck (E command).
     * @param tokens the data tokens holding the deck number, license
     *               plate, date, and time.
     */
    private void enterDeck(StringTokenizer tokens) {
        if (!hasTokens(tokens, ENTER_TOKENS)) {
            return;
        }
        Deck deck = availableDeck(tokens.nextToken());
        if (deck == null) {
            return;
        }
        String plate = tokens.nextToken();
        Vehicle vehicle = parkableVehicle(plate);
        if (vehicle == null) {
            return;
        }
        String dateToken = tokens.nextToken();
        Timestamp enter = timestamp(dateToken, tokens.nextToken());
        if (enter == null) {
            return;
        }
        if (!deck.isWithinHours(enter)) {
            System.out.println("Error entering - not within the operating "
                    + "hours: " + deck.getHour());
            return;
        }
        deck.enter(new Parking(vehicle, enter));
        System.out.println(plate + " entered Deck#" + deck.getNumber()
                + " on " + enter);
    }

    /**
     * Check if the exit time of a parking activity is valid.
     * The exit time cannot be before the entry time or more than two days
     * after the entry time.
     * @param parking the parking activity of the exiting vehicle.
     * @param exit the exit timestamp.
     * @return true if the exit time is valid; false otherwise.
     */
    private boolean isValidExit(Parking parking, Timestamp exit) {
        Timestamp enter = parking.getEnter();
        if (exit.compareTo(enter) < 0) {
            System.out.println("Exiting time " + exit
                    + " before entering time " + enter);
            return false;
        }
        if (exit.minutesSince(enter) > MAX_MINUTES_PARKED) {
            System.out.println("Invalid exiting time - exceeds two days.");
            return false;
        }
        return true;
    }

    /**
     * Record a parking activity when a vehicle exits a deck (X command).
     * The parking activity is removed from the deck and added to the
     * parking history of the vehicle.
     * @param tokens the data tokens holding the license plate, date, and
     *               time.
     */
    private void exitDeck(StringTokenizer tokens) {
        if (!hasTokens(tokens, EXIT_TOKENS)) {
            return;
        }
        String plate = tokens.nextToken();
        Vehicle vehicle = new Vehicle(plate);
        if (!isValidPlate(vehicle, plate)) {
            return;
        }
        Deck deck = deckList.getDeckWith(vehicle);
        if (deck == null) {
            System.out.println("Error exiting - " + plate
                    + " is not in a deck.");
            return;
        }
        String dateToken = tokens.nextToken();
        Timestamp exit = timestamp(dateToken, tokens.nextToken());
        if (exit == null) {
            return;
        }
        if (!deck.isWithinHours(exit)) {
            System.out.println("Error exiting - not within the operating "
                    + "hours: " + deck.getHour());
            return;
        }
        Parking parking = deck.getParking(vehicle);
        if (isValidExit(parking, exit)) {
            parking.setExit(exit);
            deck.exit(parking);
            parking.getVehicle().addHistory(parking);
            System.out.println(plate + " exited Deck#" + deck.getNumber()
                    + " on " + exit);
        }
    }

    /**
     * Display the list of open decks, or the vehicles parked in a deck if
     * a deck number is given (PD command).
     * @param tokens the data tokens, optionally holding a deck number.
     */
    private void printDecks(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            deckList.printByLocation();
            return;
        }
        Deck deck = existingDeck(tokens.nextToken());
        if (deck == null) {
            return;
        }
        if (!deck.isOpen()) {
            System.out.println("Deck#" + deck.getNumber() + " - is closed.");
        } else {
            deckList.printVehicles(deck);
        }
    }

    /**
     * Display the parking history of all vehicles, or of one vehicle if a
     * license plate is given (PH command).
     * @param tokens the data tokens, optionally holding a license plate.
     */
    private void printHistory(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            if (deckList.isEmpty()) {
                System.out.println("Deck list is empty - no deck is open.");
            } else {
                vehicleList.printHistory();
            }
            return;
        }
        String plate = tokens.nextToken();
        Vehicle vehicle = new Vehicle(plate);
        if (!isValidPlate(vehicle, plate)) {
            return;
        }
        Vehicle registered = vehicleList.getVehicle(vehicle);
        if (registered == null) {
            System.out.println(plate + " does not exist.");
        } else if (!registered.hasHistory()) {
            System.out.println(plate + " - no parking history.");
        } else {
            System.out.println("** Parking history for " + registered + "**");
            registered.printHistory();
            System.out.println("** end of list **");
        }
    }

    /**
     * Run the Parking Management System.
     * It continuously reads and processes the command lines entered on the
     * terminal until the Q command is entered; empty lines are skipped.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Parking Management System is in operation.");
        System.out.println();
        boolean running = true;
        while (running && scanner.hasNextLine()) {
            StringTokenizer tokens = new StringTokenizer(scanner.nextLine());
            if (tokens.hasMoreTokens()) {
                String command = tokens.nextToken();
                if (command.equals("Q")) {
                    System.out.println("Parking Management System is "
                            + "terminated.");
                    running = false;
                } else {
                    processCommand(command, tokens);
                }
            }
        }
        scanner.close();
    }
}
