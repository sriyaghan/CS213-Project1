package parking;

/**
 * Timestamp class that represents the date and time of a parking activity.
 * The time uses a 24-hour format with an hour and a minute.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class Timestamp implements Comparable<Timestamp> {
    /** The number of minutes in an hour. */
    private static final int MINUTES_PER_HOUR = 60;
    /** The number of hours in a day. */
    private static final int HOURS_PER_DAY = 24;
    /** The delimiter between the hour and the minute. */
    private static final String TIME_DELIMITER = ":";
    /** The result of compareTo() when this object comes first. */
    private static final int BEFORE = -1;
    /** The result of compareTo() when this object comes last. */
    private static final int AFTER = 1;
    /** The result of compareTo() when both objects are the same. */
    private static final int SAME = 0;

    /** The date of the timestamp. */
    private Date date;
    /** The hour of the timestamp, from 0 to 23. */
    private byte hour;
    /** The minute of the timestamp, from 0 to 59. */
    private byte minute;

    /**
     * Create a timestamp from a date string and a time string.
     * The time string must be in the format hh:mm.
     * @param date the date in the format yyyy-mm-dd.
     * @param time the time in the format hh:mm.
     */
    public Timestamp(String date, String time) {
        String[] parts = time.split(TIME_DELIMITER);
        this.date = new Date(date);
        this.hour = (byte) Integer.parseInt(parts[0]);
        this.minute = (byte) Integer.parseInt(parts[1]);
    }

    /**
     * Create a timestamp with the given date, hour, and minute.
     * @param date the date of the timestamp.
     * @param hour the hour of the timestamp, from 0 to 23.
     * @param minute the minute of the timestamp, from 0 to 59.
     */
    public Timestamp(Date date, int hour, int minute) {
        this.date = date;
        this.hour = (byte) hour;
        this.minute = (byte) minute;
    }

    /**
     * Get the total number of minutes from the beginning of the calendar
     * to this timestamp.
     * @return the total number of minutes.
     */
    private long totalMinutes() {
        long days = date.dayNumber();
        return (days * HOURS_PER_DAY + hour) * MINUTES_PER_HOUR + minute;
    }

    /**
     * Compare two integer values of a timestamp.
     * @param value the value of this timestamp.
     * @param otherValue the value of the other timestamp.
     * @return -1 if value is smaller, 1 if value is larger, 0 if equal.
     */
    private static int valueOrder(int value, int otherValue) {
        if (value < otherValue) {
            return BEFORE;
        }
        if (value > otherValue) {
            return AFTER;
        }
        return SAME;
    }

    /**
     * Run a test case of the compareTo() method and get the result.
     * @param testCase the test case number.
     * @param first the timestamp calling compareTo().
     * @param second the timestamp passed to compareTo().
     * @param expected the expected result of compareTo().
     * @return the description and the result of the test case.
     */
    private static String compareToTestResult(String testCase,
                                              Timestamp first,
                                              Timestamp second,
                                              int expected) {
        int actual = first.compareTo(second);
        String result = (actual == expected) ? "PASSED" : "FAILED";
        return testCase + ": (" + first + ").compareTo("
                + second + ") returns " + actual + "; expected "
                + expected + " --> " + result;
    }

    /**
     * Get the hour of the timestamp.
     * @return the hour, from 0 to 23.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Get the minute of the timestamp.
     * @return the minute, from 0 to 59.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Get the number of minutes elapsed from an earlier timestamp to this
     * timestamp.
     * @param earlier the earlier timestamp.
     * @return the number of minutes between the two timestamps; negative
     *         if the given timestamp is after this timestamp.
     */
    public long minutesSince(Timestamp earlier) {
        return totalMinutes() - earlier.totalMinutes();
    }

    /**
     * Return the textual representation of the timestamp.
     * @return the timestamp in the format yyyy-mm-dd hh:mm.
     */
    @Override
    public String toString() {
        return date + " " + String.format("%02d:%02d", hour, minute);
    }

    /**
     * Compare this timestamp with another timestamp in chronological order.
     * @param timestamp the timestamp to compare with.
     * @return -1 if this timestamp is before the given timestamp, 1 if this
     *         timestamp is after the given timestamp, and 0 if both
     *         timestamps are the same.
     */
    @Override
    public int compareTo(Timestamp timestamp) {
        int dateOrder = date.compareTo(timestamp.date);
        if (dateOrder != SAME) {
            return dateOrder;
        }
        if (hour != timestamp.hour) {
            return valueOrder(hour, timestamp.hour);
        }
        return valueOrder(minute, timestamp.minute);
    }

    /**
     * Testbed main to test the compareTo() method with the test cases in
     * the test specification.
     * @param args command-line arguments, not used.
     */
    public static void main(String[] args) {
        System.out.println(compareToTestResult("Test case #1",
                new Timestamp("2026-03-09", "10:48"),
                new Timestamp("2026-03-10", "10:48"), BEFORE));
        System.out.println(compareToTestResult("Test case #2",
                new Timestamp("2026-03-09", "09:59"),
                new Timestamp("2026-03-09", "10:00"), BEFORE));
        System.out.println(compareToTestResult("Test case #3",
                new Timestamp("2026-03-09", "10:47"),
                new Timestamp("2026-03-09", "10:48"), BEFORE));
        System.out.println(compareToTestResult("Test case #4",
                new Timestamp("2027-01-01", "00:00"),
                new Timestamp("2026-12-31", "23:59"), AFTER));
        System.out.println(compareToTestResult("Test case #5",
                new Timestamp("2026-03-09", "18:30"),
                new Timestamp("2026-03-09", "06:30"), AFTER));
        System.out.println(compareToTestResult("Test case #6",
                new Timestamp("2026-03-09", "10:49"),
                new Timestamp("2026-03-09", "10:48"), AFTER));
        System.out.println(compareToTestResult("Test case #7",
                new Timestamp("2026-03-09", "10:48"),
                new Timestamp("2026-03-09", "10:48"), SAME));
    }
}
