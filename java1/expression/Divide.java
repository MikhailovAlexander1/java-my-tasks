package expression;

public class Divide extends BinOperation {
    public Divide(SuperExpression expr1, SuperExpression expr2) {
        super(expr1, expr2, Operation.DIVISION_SIGN, Operation.DIVISION_PRIORITY);
    }

    @Override
    public int evaluate(int x) {
        return expr1.evaluate(x) / expr2.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return expr1.evaluate(x) + expr2.evaluate(x);
    }
}