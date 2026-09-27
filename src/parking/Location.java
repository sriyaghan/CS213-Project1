package parking;

/**
 * Enum class that defines the cities where the parking decks are located.
 * Each location holds the county name and the zip code of the city.
 * @author Sriya Ghankot, Alexandra Xie
 */
public enum Location {
    /** Bridgewater, Somerset County, 08807. */
    BRIDGEWATER("Somerset", "08807"),
    /** Piscataway, Middlesex County, 08854. */
    PISCATAWAY("Middlesex", "08854"),
    /** Edison, Middlesex County, 08817. */
    EDISON("Middlesex", "08817"),
    /** Princeton, Mercer County, 08542. */
    PRINCETON("Mercer", "08542"),
    /** Morristown, Morris County, 07960. */
    MORRISTOWN("Morris", "07960"),
    /** Clark, Union County, 07066. */
    CLARK("Union", "07066");

    /** The name of the county where the city is located. */
    private final String county;
    /** The zip code of the city. */
    private final String zip;

    /**
     * Create a location with the given county name and zip code.
     * @param county the name of the county.
     * @param zip the zip code of the city.
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Get the name of the county where the city is located.
     * @return the county name.
     */
    public String getCounty() {
        return county;
    }

    /**
     * Get the zip code of the city.
     * @return the zip code.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Find the location matching the given city name, ignoring case.
     * @param city the name of the city.
     * @return the matching location; null if the city is not valid.
     */
    public static Location toLocation(String city) {
        for (Location location : values()) {
            if (location.name().equalsIgnoreCase(city)) {
                return location;
            }
        }
        return null;
    }
}
