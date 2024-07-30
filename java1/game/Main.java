package game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter the number of wins to complete the game: ");
            int needToWin = in.nextInt();
            int[] wins = new int[2];
            int numberOfMatch = 1;
            boolean endCondition = false;
            String pl1Name = "Human";
            String pl2Name = "Bot";
            while (!endCondition) {
                System.out.println("Enter the field size and \"win\" condition" +
                        "(how many elements must be in a row to win): ");
                final int result;
                int m = in.nextInt();
                int n = in.nextInt();
                int k = in.nextInt();
                if (!checker(new int[] {needToWin, m, n, k})) {
                    throw new NotositiveNumbersException("You have to enter positive numbers!!!");
                }
                if (k > m && k > n) {
                    System.out.println("The game won't complete, because you entered the value" +
                            " of elements in a row to win more than the horizontal and vertical" +
                            " size of field.");
                    continue;
                }
                if (numberOfMatch % 2 == 1) {
                    result = new TwoPlayerGame(
                            new TicTacToeBoard(m, n, k),
                            new HumanPlayer(in, m, n),
                            new RandomPlayer(m, n)
                    ).play(true);
                } else {
                    result = new TwoPlayerGame(
                            new TicTacToeBoard(m, n, k),
                            new RandomPlayer(m, n),
                            new HumanPlayer(in, m, n)
                    ).play(true);
                }
                numberOfMatch++;
                switch (result) {
                    case 1:
                        if ((numberOfMatch - 1) % 2 == 1) {
                            wins[0]++;
                        } else {
                            wins[1]++;
                        }
                        System.out.println("First player won");
                        break;
                    case 2:
                        if ((numberOfMatch - 1) % 2 == 0) {
                            wins[0]++;
                        } else {
                            wins[1]++;
                        }
                        System.out.println("Second player won");
                        break;
                    case 0:
                        System.out.println("Draw");
                        break;
                    default:
                        throw new AssertionError("Unknown result " + result);
                }
                System.out.println();
                System.out.println("Current score: " + pl1Name + " " + wins[0] + " - " + pl2Name + " " + wins[1]);
                if (wins[0] == needToWin) {
                    endCondition = true;
                    System.out.println(pl1Name +
                            " won with " + wins[0] + " - " + wins[1] + " score!");
                } else if (wins[1] == needToWin) {
                    endCondition = true;
                    System.out.println(pl2Name +
                            " won with " + wins[0] + " - " + wins[1] + " score!");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("You have to enter positive number for " +
                    "wins and 3 positives numbers for field size and " +
                    "three positive numbers then." + " reason: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Game Over!!!!");
        }
    }

    private static boolean checker(int[] n) {
        for (int v : n) {
            if (v < 0) {
                return false;
            }
        }
        return true;
    }
}