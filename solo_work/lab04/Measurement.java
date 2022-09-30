public class Measurement {
    private int f, i;

    private static final int INCHES_IN_FEET = 12;
    /**
     * Constructor: initialize this object to be a measurement of 0 feet, 0
     * inches
     */
    public Measurement() {
        f = 0; i = 0;
    }

    /**
     * Constructor: takes a number of feet as its single argument, using 0 as
     * the number of inches
     */
    public Measurement(int feet) {
        f = feet;
    }

    /**
     * Constructor: takes the number of feet in the measurement and the number
     * of inches as arguments (in that order), and does the appropriate
     * initialization
     */
    public Measurement(int feet, int inches) {
        f = feet; i = inches;
    }

    /**
     * Returns the number of feet in in this Measurement. For example, if the
     * Measurement has 1 foot and 6 inches, this method should return 1.
     */
    public int getFeet() {
        return f;
    }

    /**
     * Returns the number of inches in this Measurement. For example, if the
     * Measurement has 1 foot and 6 inches, this method should return 6.
     */
    public int getInches() {
        return i;
    }

    /** Adds the argument m2 to the current measurement */
    public Measurement plus(Measurement m2) {
        int nf = f + m2.getFeet(), ni = i + m2.getInches();
        nf += ni / INCHES_IN_FEET; ni %= INCHES_IN_FEET;
        return simplify(new Measurement(nf, ni));
    }

    /**
     * Subtracts the argument m2 from the current measurement. You may assume
     * that m2 will always be smaller than the current measurement.
     */
    public Measurement minus(Measurement m2) {
        int nf = f - m2.getFeet(), ni = i - m2.getInches();
        return simplify(new Measurement(nf, ni));
    }

    /**
     * Takes a nonnegative integer argument n, and returns a new object that
     * represents the result of multiplying this object's measurement by n. For
     * example, if this object represents a measurement of 7 inches, multiple
     * (3) should return an object that represents 1 foot, 9 inches.
     */
    public Measurement multiple(int multipleAmount) {
        int nf = f * multipleAmount, ni = i * multipleAmount;
        return simplify(new Measurement(nf, ni));
    }

    private static Measurement simplify(Measurement m) {
        int nf = m.getFeet()+m.getInches()/INCHES_IN_FEET, ni = m.getInches()%INCHES_IN_FEET;
        if (ni < 0) {
            ni += INCHES_IN_FEET;
            nf--;
        }
        return new Measurement(nf, ni);
    }
    /**
     * toString should return the String representation of this object in the
     * form f'i" that is, a number of feet followed by a single quote followed
     * by a number of inches less than 12 followed by a double quote (with no
     * blanks).
     */
    @Override
    public String toString() {
        return String.format("%d'%d\"", f, i);
    }
}