package expression;

public class Add extends BinOperation {
    public Add(SuperExpression expr1, SuperExpression expr2) {
        super(expr1, expr2, Operation.PLUS_SIGN, Operation.ADD_PRIORITY);
    }

    @Override
    public int evaluate(int x) {
        return expr1.evaluate(x) + expr2.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return expr1.evaluate(x) + expr2.evaluate(x);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}