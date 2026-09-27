package parking;

/**
 * History class that defines a node in a singly linked list of the
 * parking history of a vehicle.
 * Each node holds a completed parking activity and a reference to the
 * next node in the list.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class History {
    /** The completed parking activity. */
    private Parking parking;
    /** The reference to the next node in the list. */
    private History next;

    /**
     * Create a node holding the given parking activity and next node.
     * @param parking the completed parking activity.
     * @param next the next node in the list.
     */
    public History(Parking parking, History next) {
        this.parking = parking;
        this.next = next;
    }

    /**
     * Get the parking activity held by this node.
     * @return the parking activity.
     */
    public Parking getParking() {
        return parking;
    }

    /**
     * Get the next node in the list.
     * @return the next node; null if this is the last node.
     */
    public History getNext() {
        return next;
    }

    /**
     * Set the next node in the list.
     * @param next the node to follow this node.
     */
    public void setNext(History next) {
        this.next = next;
    }

    /**
     * Print the parking activities from this node to the end of the list.
     */
    public void printHistory() {
        for (History node = this; node != null; node = node.next) {
            System.out.println(node.parking);
        }
    }
}
