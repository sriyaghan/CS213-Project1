package parking;

/**
 * VehicleList class that is a resizable array holding the registered
 * vehicles.
 * The initial capacity is 4, and it grows by 4 whenever the array is full.
 * The capacity never decreases.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class VehicleList {
    /** The initial capacity and growth amount of the array. */
    private static final int LENGTH = 4;
    /** The index returned by find() if the item is not in the list. */
    private static final int NOTFOUND = -1;

    /** The array holding the registered vehicles. */
    private Vehicle[] vehicles;
    /** The number of vehicles in the array. */
    private int numVehicles;

    /**
     * Create an empty vehicle list with the initial capacity.
     */
    public VehicleList() {
        vehicles = new Vehicle[LENGTH];
        numVehicles = 0;
    }

    /**
     * Find a vehicle in the array.
     * @param vehicle the vehicle to find.
     * @return the index of the vehicle; NOTFOUND if it is not in the list.
     */
    private int find(Vehicle vehicle) {
        for (int i = 0; i < numVehicles; i++) {
            if (vehicles[i].equals(vehicle)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Increase the capacity of the array by 4.
     */
    private void grow() {
        Vehicle[] larger = new Vehicle[vehicles.length + LENGTH];
        for (int i = 0; i < numVehicles; i++) {
            larger[i] = vehicles[i];
        }
        vehicles = larger;
    }

    /**
     * Sort the vehicles by license plate in place with insertion sort.
     */
    private void sortByPlate() {
        for (int i = 1; i < numVehicles; i++) {
            Vehicle current = vehicles[i];
            int position = i - 1;
            while (position >= 0 && vehicles[position].getPlate()
                    .compareTo(current.getPlate()) > 0) {
                vehicles[position + 1] = vehicles[position];
                position--;
            }
            vehicles[position + 1] = current;
        }
    }

    /**
     * Add a vehicle to the end of the array, growing it if it is full.
     * @param vehicle the vehicle to add.
     */
    public void add(Vehicle vehicle) {
        if (numVehicles == vehicles.length) {
            grow();
        }
        vehicles[numVehicles] = vehicle;
        numVehicles++;
    }

    /**
     * Remove a vehicle by replacing it with the last element in the array.
     * It does nothing if the vehicle is not in the list.
     * @param vehicle the vehicle to remove.
     */
    public void remove(Vehicle vehicle) {
        int index = find(vehicle);
        if (index == NOTFOUND) {
            return;
        }
        vehicles[index] = vehicles[numVehicles - 1];
        vehicles[numVehicles - 1] = null;
        numVehicles--;
    }

    /**
     * Check if a vehicle is in the list.
     * @param vehicle the vehicle to check.
     * @return true if the vehicle is in the list; false otherwise.
     */
    public boolean contains(Vehicle vehicle) {
        return find(vehicle) != NOTFOUND;
    }

    /**
     * Get the registered vehicle with the same license plate as the given
     * vehicle.
     * @param vehicle the vehicle holding the license plate to look for.
     * @return the registered vehicle; null if it is not in the list.
     */
    public Vehicle getVehicle(Vehicle vehicle) {
        int index = find(vehicle);
        if (index == NOTFOUND) {
            return null;
        }
        return vehicles[index];
    }

    /**
     * Display the list of registered vehicles, ordered by license plate.
     */
    public void printByPlate() {
        if (numVehicles == 0) {
            System.out.println("Vehicle list is empty - no vehicle is "
                    + "registered.");
            return;
        }
        sortByPlate();
        System.out.println("** List of registered vehicles, ordered by "
                + "license plate **");
        for (int i = 0; i < numVehicles; i++) {
            System.out.println(vehicles[i]);
        }
        System.out.println("** end of list **");
    }

    /**
     * Display the parking history of all vehicles, ordered by license
     * plate and then by the entry timestamps in descending order.
     */
    public void printHistory() {
        sortByPlate();
        System.out.println("** Parking history for all vehicles, ordered "
                + "by plate/timestamp **");
        for (int i = 0; i < numVehicles; i++) {
            vehicles[i].printHistory();
        }
        System.out.println("** end of parking history **");
    }
}
