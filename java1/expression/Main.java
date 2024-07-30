package expression;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);
        int x = ss.nextInt();
        SuperExpression value = new Add(
                new Subtract(
                        new Multiply(new Variable("x"), new Variable("x")),
                        new Multiply(new Const(2), new Variable("x"))
                ),
                new Const(1));
        int result = value.evaluate(x);
        System.out.println(result);
    }
}