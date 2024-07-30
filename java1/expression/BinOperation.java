package expression;

public abstract class BinOperation
        extends Operation
        implements SuperExpression {
    private static final char SPACE_CHAR = ' ';
    private static final char OPEN_BRACKET = '(';
    private static final char CLOSED_BRACKET = ')';
    protected final SuperExpression expr1;
    protected final SuperExpression expr2;

    public BinOperation(SuperExpression expr1, SuperExpression expr2,
                        String operationSign, int priority) {
        super(operationSign, priority);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public String toString() {
        return OPEN_BRACKET + expr1.toString() + SPACE_CHAR +
                super.toString() + SPACE_CHAR +
                expr2.toString() + CLOSED_BRACKET;
    }

    @Override
    public String toMiniString() {
        String left = expr1.toMiniString();;
        String right = expr2.toMiniString();
        if (expr1 instanceof BinOperation) {
            if (priority > ((BinOperation) expr1).priority) {
                left = OPEN_BRACKET + left + CLOSED_BRACKET;
            }
        }
        if (expr2 instanceof BinOperation) {
            BinOperation rightOp = (BinOperation) expr2;
            if (priority > rightOp.priority ||
                    (priority == rightOp.priority &&
                            (!sign.equals(rightOp.sign) || !rightOp.isAssociative))) {
                right = OPEN_BRACKET + right + CLOSED_BRACKET;
            }
        }
        return left + SPACE_CHAR + super.toString() +
                SPACE_CHAR + right;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BinOperation that = (BinOperation) obj;
        return this.sign.equals(that.sign) && this.priority == that.priority &&
                expr1.equals(that.expr1) && expr2.equals(that.expr2);
    }
}