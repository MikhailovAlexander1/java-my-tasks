package expression;

public class Subtract extends BinOperation {
    public Subtract(SuperExpression expr1, SuperExpression expr2) {
        super(expr1, expr2, Operation.MINUS_SIGN, Operation.SUBTRACT_PRIORITY);
    }

    @Override
    public int evaluate(int x) {
        return expr1.evaluate(x) - expr2.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return expr1.evaluate(x) + expr2.evaluate(x);
    }
}
