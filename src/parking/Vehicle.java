package parking;

/**
 * Vehicle class that represents a vehicle registered for parking.
 * A vehicle is uniquely identified by its license plate and keeps its
 * parking history in a singly linked list.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class Vehicle {
    /** The license plate format; X is a letter and d is a digit. */
    private static final String PLATE_FORMAT = "Xdd-XXX";
    /** The character in the plate format that stands for a letter. */
    private static final char LETTER = 'X';
    /** The character in the plate format that stands for a digit. */
    private static final char DIGIT = 'd';

    /** The license plate, exactly 7 characters. */
    private String plate;
    /** The head of the singly linked list of the parking history. */
    private History history;

    /**
     * Create a vehicle with the given license plate and no history.
     * The plate is stored in uppercase letters.
     * @param plate the license plate of the vehicle.
     */
    public Vehicle(String plate) {
        this.plate = plate.toUpperCase();
        this.history = null;
    }

    /**
     * Check if a character matches the expected character in the format.
     * @param expected the character in the plate format.
     * @param actual the character in the license plate.
     * @return true if the character matches the format; false otherwise.
     */
    private static boolean isMatch(char expected, char actual) {
        if (expected == LETTER) {
            return actual >= 'A' && actual <= 'Z';
        }
        if (expected == DIGIT) {
            return actual >= '0' && actual <= '9';
        }
        return actual == expected;
    }

    /**
     * Check if the license plate is in the valid format Xdd-XXX, where X
     * is an alphabetic letter and d is a digit.
     * @return true if the license plate is valid; false otherwise.
     */
    public boolean isValid() {
        if (plate.length() != PLATE_FORMAT.length()) {
            return false;
        }
        for (int i = 0; i < plate.length(); i++) {
            if (!isMatch(PLATE_FORMAT.charAt(i), plate.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the license plate of the vehicle.
     * @return the license plate.
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Get the head of the parking history list.
     * @return the first node of the history; null if there is no history.
     */
    public History getHistory() {
        return history;
    }

    /**
     * Check if the vehicle has any parking history.
     * @return true if the vehicle has a parking history; false otherwise.
     */
    public boolean hasHistory() {
        return history != null;
    }

    /**
     * Add a completed parking activity to the parking history.
     * The history is kept in descending order of the entry timestamps.
     * @param parking the completed parking activity.
     */
    public void addHistory(Parking parking) {
        Timestamp enter = parking.getEnter();
        if (history == null
                || enter.compareTo(history.getParking().getEnter()) >= 0) {
            history = new History(parking, history);
            return;
        }
        History current = history;
        while (current.getNext() != null && enter.compareTo(
                current.getNext().getParking().getEnter()) < 0) {
            current = current.getNext();
        }
        current.setNext(new History(parking, current.getNext()));
    }

    /**
     * Print the parking history of the vehicle, ordered by the entry
     * timestamps in descending order.
     */
    public void printHistory() {
        if (history != null) {
            history.printHistory();
        }
    }

    /**
     * Check if this vehicle is equal to another vehicle.
     * @param obj the object to compare with.
     * @return true if both vehicles have the same license plate;
     *         false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) obj;
            return plate.equals(vehicle.plate);
        }
        return false;
    }

    /**
     * Return the textual representation of the vehicle.
     * @return the license plate of the vehicle.
     */
    @Override
    public String toString() {
        return plate;
    }
}
