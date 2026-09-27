package parking;

/**
 * DeckList class that is a resizable array holding the parking decks.
 * The initial capacity is 4, and it grows by 4 whenever the array is full.
 * The capacity never decreases, and closed decks remain in the list.
 * @author Sriya Ghankot, Alexandra Xie
 */
public class DeckList {
    /** The initial capacity and growth amount of the array. */
    private static final int ARRAYLENGTH = 4;
    /** The index returned by find() if the item is not in the list. */
    private static final int NOTFOUND = -1;

    /** The array holding the decks. */
    private Deck[] decks;
    /** The number of decks in the array. */
    private int numDecks;

    /**
     * Create an empty deck list with the initial capacity.
     */
    public DeckList() {
        decks = new Deck[ARRAYLENGTH];
        numDecks = 0;
    }

    /**
     * Find a deck in the array.
     * @param deck the deck to find.
     * @return the index of the deck; NOTFOUND if it is not in the list.
     */
    private int find(Deck deck) {
        for (int i = 0; i < numDecks; i++) {
            if (decks[i].equals(deck)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Increase the capacity of the array by 4.
     */
    private void grow() {
        Deck[] larger = new Deck[decks.length + ARRAYLENGTH];
        for (int i = 0; i < numDecks; i++) {
            larger[i] = decks[i];
        }
        decks = larger;
    }

    /**
     * Check if a deck should be listed before another deck, ordered by
     * county and then by deck number.
     * @param first the first deck.
     * @param second the second deck.
     * @return true if the first deck comes before the second deck;
     *         false otherwise.
     */
    private boolean isBefore(Deck first, Deck second) {
        int countyOrder = first.getLocation().getCounty()
                .compareTo(second.getLocation().getCounty());
        if (countyOrder != 0) {
            return countyOrder < 0;
        }
        return first.getNumber() < second.getNumber();
    }

    /**
     * Sort the decks by county and then by deck number in place with
     * insertion sort.
     */
    private void sortByLocation() {
        for (int i = 1; i < numDecks; i++) {
            Deck current = decks[i];
            int position = i - 1;
            while (position >= 0 && isBefore(current, decks[position])) {
                decks[position + 1] = decks[position];
                position--;
            }
            decks[position + 1] = current;
        }
    }

    /**
     * Get the number of decks that are open.
     * @return the number of open decks.
     */
    private int openCount() {
        int count = 0;
        for (int i = 0; i < numDecks; i++) {
            if (decks[i].isOpen()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Open a deck for operation.
     * A new deck is added to the end of the array; a closed deck already
     * in the list is reopened.
     * @param deck the deck to open.
     */
    public void open(Deck deck) {
        int index = find(deck);
        if (index != NOTFOUND) {
            decks[index].setOpen(true);
            return;
        }
        if (numDecks == decks.length) {
            grow();
        }
        decks[numDecks] = deck;
        numDecks++;
    }

    /**
     * Mark a deck as closed; the deck remains in the list.
     * @param deck the deck to close.
     */
    public void close(Deck deck) {
        int index = find(deck);
        if (index != NOTFOUND) {
            decks[index].setOpen(false);
        }
    }

    /**
     * Check if a deck is in the list.
     * @param deck the deck to check.
     * @return true if the deck is in the list; false otherwise.
     */
    public boolean contains(Deck deck) {
        return find(deck) != NOTFOUND;
    }

    /**
     * Get the deck in the list with the same deck number as the given deck.
     * @param deck the deck holding the deck number to look for.
     * @return the deck in the list; null if it is not in the list.
     */
    public Deck getDeck(Deck deck) {
        int index = find(deck);
        if (index == NOTFOUND) {
            return null;
        }
        return decks[index];
    }

    /**
     * Get the deck where a vehicle is recorded as entered without an exit.
     * @param vehicle the vehicle to look for.
     * @return the deck the vehicle is parked in; null if the vehicle is
     *         not parked in any deck.
     */
    public Deck getDeckWith(Vehicle vehicle) {
        for (int i = 0; i < numDecks; i++) {
            if (decks[i].getParking(vehicle) != null) {
                return decks[i];
            }
        }
        return null;
    }

    /**
     * Check if the deck list is empty.
     * @return true if there is no deck in the list; false otherwise.
     */
    public boolean isEmpty() {
        return numDecks == 0;
    }

    /**
     * Display the list of open decks, ordered by county and then by deck
     * number.
     */
    public void printByLocation() {
        if (openCount() == 0) {
            System.out.println("Deck list is empty - no deck is open.");
            return;
        }
        sortByLocation();
        System.out.println("** List of decks, ordered by county/deck "
                + "number **");
        for (int i = 0; i < numDecks; i++) {
            if (decks[i].isOpen()) {
                System.out.println(decks[i]);
            }
        }
        System.out.println("** end of list **");
    }

    /**
     * Display the list of vehicles parked in a deck, ordered by plate.
     * It does nothing if the deck is not in the list.
     * @param deck the deck whose vehicles are displayed.
     */
    public void printVehicles(Deck deck) {
        int index = find(deck);
        if (index != NOTFOUND) {
            decks[index].printVehicles();
        }
    }
}
