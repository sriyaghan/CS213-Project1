package parking;

import java.util.Calendar;

/**
 * Date class that represents a calendar date with a year, month, and day.
 * It is used as part of the timestamp of a parking activity and can check
 * whether the date is a valid calendar date.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class Date implements Comparable<Date> {
    /** Every 4 years is a candidate for a leap year. */
    private static final int QUADRENNIAL = 4;
    /** Years divisible by 100 are not leap years unless divisible by 400. */
    private static final int CENTENNIAL = 100;
    /** Years divisible by 400 are leap years. */
    private static final int QUATERCENTENNIAL = 400;
    /** The month number of January. */
    private static final int JAN = Calendar.JANUARY + 1;
    /** The month number of February. */
    private static final int FEB = Calendar.FEBRUARY + 1;
    /** The month number of April. */
    private static final int APR = Calendar.APRIL + 1;
    /** The month number of June. */
    private static final int JUN = Calendar.JUNE + 1;
    /** The month number of September. */
    private static final int SEP = Calendar.SEPTEMBER + 1;
    /** The month number of November. */
    private static final int NOV = Calendar.NOVEMBER + 1;
    /** The month number of December. */
    private static final int DEC = Calendar.DECEMBER + 1;
    /** The number of days in a 31-day month. */
    private static final int DAYS_IN_LONG_MONTH = 31;
    /** The number of days in a 30-day month. */
    private static final int DAYS_IN_SHORT_MONTH = 30;
    /** The number of days in February of a non-leap year. */
    private static final int DAYS_IN_FEB = 28;
    /** The number of days in February of a leap year. */
    private static final int DAYS_IN_LEAP_FEB = 29;
    /** The number of days in a non-leap year. */
    private static final int DAYS_IN_YEAR = 365;
    /** The number of parts in a date string yyyy-mm-dd. */
    private static final int DATE_PARTS = 3;
    /** The index of the day in the parts of a date string. */
    private static final int DAY_INDEX = 2;
    /** The delimiter between the year, month, and day. */
    private static final String DATE_DELIMITER = "-";
    /** The result of compareTo() when this object comes first. */
    private static final int BEFORE = -1;
    /** The result of compareTo() when this object comes last. */
    private static final int AFTER = 1;
    /** The result of compareTo() when both objects are the same. */
    private static final int SAME = 0;

    /** The year of the date. */
    private int year;
    /** The month of the date, from 1 to 12. */
    private int month;
    /** The day of the month. */
    private int day;

    /**
     * Create a date from a string in the format yyyy-mm-dd.
     * If the string is not in this format, the date is set to an invalid
     * date, which can be detected by isValid().
     * @param date the date as a string, such as 2026-03-09.
     */
    public Date(String date) {
        String[] parts = date.split(DATE_DELIMITER);
        if (parts.length == DATE_PARTS) {
            try {
                int parsedYear = Integer.parseInt(parts[0]);
                int parsedMonth = Integer.parseInt(parts[1]);
                int parsedDay = Integer.parseInt(parts[DAY_INDEX]);
                year = parsedYear;
                month = parsedMonth;
                day = parsedDay;
            } catch (NumberFormatException exception) {
                month = 0; //marks the date as invalid
            }
        }
    }

    /**
     * Check if the given year is a leap year.
     * @param calendarYear the year to check.
     * @return true if the year is a leap year; false otherwise.
     */
    private static boolean isLeap(int calendarYear) {
        if (calendarYear % QUADRENNIAL != 0) {
            return false;
        }
        if (calendarYear % CENTENNIAL != 0) {
            return true;
        }
        return calendarYear % QUATERCENTENNIAL == 0;
    }

    /**
     * Get the number of days in the given month of the given year.
     * @param monthOfYear the month, from 1 to 12.
     * @param calendarYear the year the month belongs to.
     * @return the number of days in the month.
     */
    private static int daysInMonth(int monthOfYear, int calendarYear) {
        if (monthOfYear == FEB) {
            return isLeap(calendarYear) ? DAYS_IN_LEAP_FEB : DAYS_IN_FEB;
        }
        if (monthOfYear == APR || monthOfYear == JUN || monthOfYear == SEP
                || monthOfYear == NOV) {
            return DAYS_IN_SHORT_MONTH;
        }
        return DAYS_IN_LONG_MONTH;
    }

    /**
     * Compare two integer values of a date.
     * @param value the value of this date.
     * @param otherValue the value of the other date.
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
     * Run a test case of the isValid() method and get the result.
     * @param testCase the test case number.
     * @param input the date to test, in the format yyyy-mm-dd.
     * @param expected the expected result of isValid().
     * @return the description and the result of the test case.
     */
    private static String isValidTestResult(String testCase,
                                            String input,
                                            boolean expected) {
        boolean actual = new Date(input).isValid();
        String result = (actual == expected) ? "PASSED" : "FAILED";
        return testCase + ": isValid() on " + input
                + " returns " + actual + "; expected " + expected
                + " --> " + result;
    }

    /**
     * Check if the date is a valid calendar date.
     * The month must be from 1 to 12, and the day must be from 1 to the
     * number of days in the month; February has 29 days in a leap year.
     * @return true if the date is a valid calendar date; false otherwise.
     */
    public boolean isValid() {
        if (year < 1 || month < JAN || month > DEC || day < 1) {
            return false;
        }
        return day <= daysInMonth(month, year);
    }

    /**
     * Get the number of days from the beginning of the calendar to this
     * date, counting this date as a day.
     * @return the day number of the date.
     */
    public long dayNumber() {
        long priorYears = year - 1;
        long days = priorYears * DAYS_IN_YEAR + priorYears / QUADRENNIAL
                - priorYears / CENTENNIAL + priorYears / QUATERCENTENNIAL;
        for (int i = JAN; i < month; i++) {
            days += daysInMonth(i, year);
        }
        return days + day;
    }

    /**
     * Check if this date is equal to another date.
     * @param obj the object to compare with.
     * @return true if both dates have the same year, month, and day;
     *         false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            return year == date.year && month == date.month
                    && day == date.day;
        }
        return false;
    }

    /**
     * Return the textual representation of the date.
     * @return the date in the format yyyy-mm-dd.
     */
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Compare this date with another date in chronological order.
     * @param date the date to compare with.
     * @return -1 if this date is before the given date, 1 if this date is
     *         after the given date, and 0 if both dates are the same.
     */
    @Override
    public int compareTo(Date date) {
        if (year != date.year) {
            return valueOrder(year, date.year);
        }
        if (month != date.month) {
            return valueOrder(month, date.month);
        }
        return valueOrder(day, date.day);
    }

    /**
     * Testbed main to test the isValid() method with the test cases in the
     * test specification.
     * @param args command-line arguments, not used.
     */
    public static void main(String[] args) {
        System.out.println(isValidTestResult("Test case #1", "2026-13-09",
                false));
        System.out.println(isValidTestResult("Test case #2", "2026-04-31",
                false));
        System.out.println(isValidTestResult("Test case #3", "2026-02-29",
                false));
        System.out.println(isValidTestResult("Test case #4", "1900-02-29",
                false));
        System.out.println(isValidTestResult("Test case #5", "2000-02-29",
                true));
        System.out.println(isValidTestResult("Test case #6", "2026-03-31",
                true));
    }
}
