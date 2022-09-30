public class GregorianDate extends Date {

    private static final int[] MONTH_LENGTHS = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }


    // YOUR CODE HERE

    @Override
    public int dayOfYear() {
        int precedingMonthDays = 0;
        for (int m = 1; m < month; m += 1) {
            precedingMonthDays += getMonthLength(m);
        }
        return precedingMonthDays + dayOfMonth;
    }

    @Override
    public Date nextDate() {
        int newDay = dayOfMonth, newMonth = month, newYear = year;
        newDay++;
        if (newDay > getMonthLength(month)) {
            newDay = 1;
            newMonth++;
        }
        if (newMonth > 12) {
            newMonth = 1;
            newYear++;
        }
        return new GregorianDate(newYear, newMonth, newDay);
    }

    private static int getMonthLength(int m) {
        return MONTH_LENGTHS[m - 1];
    }
}