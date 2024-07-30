package expression;

public class Operation {
    public static final String PLUS_SIGN = "+";
    public static final String MINUS_SIGN = "-";
    public static final String MULTIPLICATION_SIGN = "*";
    public static final String DIVISION_SIGN = "/";
    public static final int ADD_PRIORITY = 1;
    public static final int SUBTRACT_PRIORITY = 1;
    public static final int MULTIPLICATION_PRIORITY = 2;
    public static final int DIVISION_PRIORITY  = 2;
    public static Operation newAdd() {
        return new Operation(PLUS_SIGN, ADD_PRIORITY);
    }
    public static Operation newSubtract() {
        return new Operation(MINUS_SIGN, SUBTRACT_PRIORITY);
    }
    public static Operation newMultiplication() {
        return new Operation(MULTIPLICATION_SIGN, MULTIPLICATION_PRIORITY);
    }
    public static Operation newDivision() {
        return new Operation(DIVISION_SIGN, DIVISION_PRIORITY);
    }
    protected final String sign;
    protected final int priority;
    protected final boolean isAssociative;

    Operation(final String sign, final int priority) {
        this.sign = sign;
        this.priority = priority;
        if (sign.equals(MINUS_SIGN) || sign.equals(DIVISION_SIGN)) {
            this.isAssociative = false;
            return;
        }
        this.isAssociative = true;
    }

    @Override
    public String toString() {
        return sign;
    }
}
