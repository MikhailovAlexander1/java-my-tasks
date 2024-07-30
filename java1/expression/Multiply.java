package expression;

public class Multiply extends BinOperation {
    public Multiply(SuperExpression expr1, SuperExpression expr2) {
        super(expr1, expr2, Operation.MULTIPLICATION_SIGN, Operation.MULTIPLICATION_PRIORITY);
    }

    @Override
    public int evaluate(int x) {
        return expr1.evaluate(x) * expr2.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return expr1.evaluate(x) + expr2.evaluate(x);
    }
}