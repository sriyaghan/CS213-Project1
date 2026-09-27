package parking;

/**
 * Parking class that defines a parking activity of a vehicle in a deck.
 * The activity records the entry timestamp and, once the vehicle leaves
 * the deck, the exit timestamp.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class Parking {
    /** The vehicle that is parked. */
    private Vehicle vehicle;
    /** The timestamp when the vehicle entered the deck. */
    private Timestamp enter;
    /** The timestamp when the vehicle exited the deck; null if parked. */
    private Timestamp exit;

    /**
     * Create a parking activity for a vehicle entering a deck.
     * @param vehicle the vehicle entering the deck.
     * @param enter the timestamp when the vehicle entered the deck.
     */
    public Parking(Vehicle vehicle, Timestamp enter) {
        this.vehicle = vehicle;
        this.enter = enter;
        this.exit = null;
    }

    /**
     * Get the vehicle of the parking activity.
     * @return the vehicle.
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Get the timestamp when the vehicle entered the deck.
     * @return the entry timestamp.
     */
    public Timestamp getEnter() {
        return enter;
    }

    /**
     * Record the timestamp when the vehicle exited the deck.
     * @param exit the exit timestamp.
     */
    public void setExit(Timestamp exit) {
        this.exit = exit;
    }

    /**
     * Return the textual representation of the parking activity.
     * @return the parking activity in the format
     *         R48-JIK [entered: 2026-03-09 10:48][exited: 2026-03-09 11:50]
     */
    @Override
    public String toString() {
        return vehicle + " [entered: " + enter + "][exited: " + exit + "]";
    }
}
