package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random = new Random();
    int m;
    int n;
    public int first;
    public int second;

    public RandomPlayer(int m, int n) {
        this.m = m;
        this.n = n;
    }

    @Override
    public Move makeMove(Position position) {
        while (true) {
            final Move move = new Move(
                    first = random.nextInt(this.m),
                    second = random.nextInt(this.n),
                    position.getTurn()
            );
            if (position.isValid(move)) {
                return move;
            }

        }
    }

    @Override
    public int getFirst() {
        return this.first;
    }

    @Override
    public int getSecond() {
        return this.second;
    }
}

/*
package game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;
    private int returnCondition = 0;
    public int first;
    public int second;
    int m;
    int n;

    public HumanPlayer(Scanner in, int m, int n) {
        this.in = in;
        this.m = m;
        this.n = n;
    }

    @Override
    public Move makeMove(Position position) {
        System.out.println();
        System.out.println("Current position");
        System.out.println(position);
        System.out.println("Enter you move for " + position.getTurn() + " (two positive numbers with space) :");
        while (true) {
            try {
                boolean t = checker();
                if (!t && returnCondition == 1) {
                    return new Move(0, 0, Cell.X == position.getTurn() ? Cell.O : Cell.X);
                } else if (t) {
                    Move move = new Move(first, second, position.getTurn());
                    if (position.isValid(move)) {
                        return move;
                    } else {
                        throw new NotValidHumanMoveException("Incorrect coordinates (out of field)" +
                                " or this place is already taken, try again.");
                    }
                }
            } catch (NotValidHumanMoveException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public int getFirst() {
        return this.first;
    }

    @Override
    public int getSecond() {
        return this.second;
    }

    private boolean checker() {
        try {
            int count = 0;
            String cord = in.nextLine();
            Scanner ss = new Scanner(cord);
            int buf;
            while (ss.hasNextInt()) {
                buf = ss.nextInt();
                if (count == 0) {
                    first = buf - 1;
                } else if (count == 1) {
                    second = buf - 1;
                }
                count++;
            }
            if (count > 2) {
                throw new NotTwoNumbersInInputDataException("You have to enter TWO numbers " +
                        "(first have to be in range: 0 - " + m + ", second: 0 - " + n + ")");
            }
            if (ss.hasNext()) {
                String statement = ss.next();
                if (statement.equals("return")) {
                    returnCondition++;
                    if (returnCondition > 1) {
                        throw new CheatingException("You can't back to previous turn" +
                                " more than one time to avoid cheating!");
                    }
                    return false;
                } else {
                    throw new InputMismatchException("You enter wrong type of input data, " +
                            "please enter positive numbers!");
                }
            }
            return count == 2;
        } catch (CheatingException | InputMismatchException | NotTwoNumbersInInputDataException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (Exception any) {
            System.out.println(any.getMessage());
            return false;
        }
    }
}
 */