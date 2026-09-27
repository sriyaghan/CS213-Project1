package parking;

/**
 * Enum class that defines the operating hours of the parking decks.
 * The hours use a 24-hour time format; each code maps to an opening
 * time and a closing time.
 * @author Sriya Ghankot, Alexandra Xie
 */
public enum Hour {
    /** Open from 5:00 to 21:30. */
    HR5(5, 0, 21, 30),
    /** Open from 6:30 to 18:30. */
    HR6(6, 30, 18, 30),
    /** Open from 7:00 to 18:00. */
    HR7(7, 0, 18, 0);

    /** The number of minutes in an hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The hour the deck opens. */
    private final int openHour;
    /** The minute the deck opens. */
    private final int openMinute;
    /** The hour the deck closes. */
    private final int closeHour;
    /** The minute the deck closes. */
    private final int closeMinute;

    /**
     * Create the operating hours with the given opening and closing times.
     * @param openHour the hour the deck opens.
     * @param openMinute the minute the deck opens.
     * @param closeHour the hour the deck closes.
     * @param closeMinute the minute the deck closes.
     */
    Hour(int openHour, int openMinute, int closeHour, int closeMinute) {
        this.openHour = openHour;
        this.openMinute = openMinute;
        this.closeHour = closeHour;
        this.closeMinute = closeMinute;
    }

    /**
     * Get the textual representation of a time in the format h:mm.
     * @param hour the hour of the time.
     * @param minute the minute of the time.
     * @return the time as a string, such as 6:30.
     */
    private static String clockTime(int hour, int minute) {
        return hour + ":" + String.format("%02d", minute);
    }

    /**
     * Check if the time of a timestamp is within the operating hours.
     * The opening and closing times are both included.
     * @param timestamp the timestamp to check.
     * @return true if the time is within the operating hours;
     *         false otherwise.
     */
    public boolean isWithin(Timestamp timestamp) {
        int time = timestamp.getHour() * MINUTES_PER_HOUR
                + timestamp.getMinute();
        int openTime = openHour * MINUTES_PER_HOUR + openMinute;
        int closeTime = closeHour * MINUTES_PER_HOUR + closeMinute;
        return time >= openTime && time <= closeTime;
    }

    /**
     * Find the operating hours matching the given code, ignoring case.
     * @param code the code of the operating hours, such as HR5.
     * @return the matching operating hours; null if the code is not valid.
     */
    public static Hour toHour(String code) {
        for (Hour hour : values()) {
            if (hour.name().equalsIgnoreCase(code)) {
                return hour;
            }
        }
        return null;
    }

    /**
     * Return the textual representation of the operating hours.
     * @return the operating hours in the format 6:30 ~ 18:30.
     */
    @Override
    public String toString() {
        return clockTime(openHour, openMinute) + " ~ "
                + clockTime(closeHour, closeMinute);
    }
}
