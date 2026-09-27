package parking;

/**
 * Deck class that holds the information of a parking deck.
 * A deck has a number, a location, operating hours, a maximum capacity,
 * and the list of parking activities of the vehicles currently parked.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class Deck {
    /** The index returned by find() if the item is not in the list. */
    private static final int NOTFOUND = -1;
    /** The maximum number of vehicles a deck can hold. */
    public static final int MAXCAPACITY = 6;

    /** The deck number. */
    private int number;
    /** The location of the deck. */
    private Location location;
    /** The operating hours of the deck. */
    private Hour hour;
    /** The parking activities of the vehicles parked in the deck. */
    private Parking[] parkings;
    /** The number of parking activities in the array. */
    private int numParked;
    /** Whether the deck is open for operation. */
    private boolean open;

    /**
     * Create a deck with only the deck number, used to look up a deck.
     * @param number the deck number.
     */
    public Deck(int number) {
        this.number = number;
    }

    /**
     * Create an open deck with the given information.
     * @param number the deck number.
     * @param location the location of the deck.
     * @param hour the operating hours of the deck.
     * @param capacity the maximum number of vehicles the deck can hold.
     */
    public Deck(int number, Location location, Hour hour, int capacity) {
        this.number = number;
        this.location = location;
        this.hour = hour;
        this.parkings = new Parking[capacity];
        this.numParked = 0;
        this.open = true;
    }

    /**
     * Find the parking activity of a vehicle in the array.
     * @param vehicle the vehicle to find.
     * @return the index of the parking activity; NOTFOUND if the vehicle
     *         is not parked in the deck.
     */
    private int find(Vehicle vehicle) {
        for (int i = 0; i < numParked; i++) {
            if (parkings[i].getVehicle().equals(vehicle)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Sort the parking activities by license plate in place with
     * insertion sort.
     */
    private void sortByPlate() {
        for (int i = 1; i < numParked; i++) {
            Parking current = parkings[i];
            String plate = current.getVehicle().getPlate();
            int position = i - 1;
            while (position >= 0 && parkings[position].getVehicle().getPlate()
                    .compareTo(plate) > 0) {
                parkings[position + 1] = parkings[position];
                position--;
            }
            parkings[position + 1] = current;
        }
    }

    /**
     * Add a parking activity to the array when a vehicle enters the deck.
     * It does nothing if the deck is full.
     * @param parking the parking activity to add.
     */
    public void enter(Parking parking) {
        if (isFull()) {
            return;
        }
        parkings[numParked] = parking;
        numParked++;
    }

    /**
     * Remove a parking activity from the array when a vehicle exits the
     * deck by replacing it with the last element in the array.
     * @param parking the parking activity to remove.
     */
    public void exit(Parking parking) {
        int index = find(parking.getVehicle());
        if (index == NOTFOUND) {
            return;
        }
        parkings[index] = parkings[numParked - 1];
        parkings[numParked - 1] = null;
        numParked--;
    }

    /**
     * Get the parking activity of a vehicle parked in the deck.
     * @param vehicle the vehicle to look for.
     * @return the parking activity; null if the vehicle is not parked in
     *         the deck.
     */
    public Parking getParking(Vehicle vehicle) {
        int index = find(vehicle);
        if (index == NOTFOUND) {
            return null;
        }
        return parkings[index];
    }

    /**
     * Get the deck number.
     * @return the deck number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Get the location of the deck.
     * @return the location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get the operating hours of the deck.
     * @return the operating hours.
     */
    public Hour getHour() {
        return hour;
    }

    /**
     * Get the number of vehicles parked in the deck.
     * @return the number of vehicles parked.
     */
    public int getNumParked() {
        return numParked;
    }

    /**
     * Check if the deck is open for operation.
     * @return true if the deck is open; false if it is closed.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Open or close the deck for operation.
     * @param open true to open the deck; false to close it.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Check if the deck has reached its maximum capacity.
     * @return true if the deck is full; false otherwise.
     */
    public boolean isFull() {
        return numParked == parkings.length;
    }

    /**
     * Check if a timestamp is within the operating hours of the deck.
     * @param timestamp the timestamp to check.
     * @return true if the time is within the operating hours;
     *         false otherwise.
     */
    public boolean isWithinHours(Timestamp timestamp) {
        return hour.isWithin(timestamp);
    }

    /**
     * Display the list of vehicles parked in the deck, ordered by plate.
     */
    public void printVehicles() {
        if (numParked == 0) {
            System.out.println("Deck#" + number + " - no vehicle is parked.");
            return;
        }
        sortByPlate();
        System.out.println("** List of vehicles in Deck# " + number
                + ", ordered by plate **");
        for (int i = 0; i < numParked; i++) {
            System.out.println(parkings[i]);
        }
        System.out.println("** end of list **");
    }

    /**
     * Check if this deck is equal to another deck.
     * @param obj the object to compare with.
     * @return true if both decks have the same deck number; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Deck) {
            Deck deck = (Deck) obj;
            return number == deck.number;
        }
        return false;
    }

    /**
     * Return the textual representation of the deck.
     * @return the deck in the format Deck#301@PRINCETON[open 6:30 ~ 18:30]
     *         [capacity 5] [0 vehicles] [Mercer]
     */
    @Override
    public String toString() {
        String status = open ? "open" : "closed";
        String vehicles = (numParked == 1) ? " vehicle]" : " vehicles]";
        return "Deck#" + number + "@" + location + "[" + status + " "
                + hour + "] [capacity " + parkings.length + "] ["
                + numParked + vehicles + " [" + location.getCounty() + "]";
    }
}
