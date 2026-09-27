package parking;

/**
 * Driver class that starts the Parking Management System.
 * The TAs run this class to grade Project 1.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class RunProject1 {

    /**
     * Default constructor that creates a driver for Project 1.
     */
    public RunProject1() {
    }

    /**
     * Start the Parking Management System by running the user interface.
     * @param args command-line arguments, not used.
     */
    public static void main(String[] args) {
        new Operation().run();
    }
}
