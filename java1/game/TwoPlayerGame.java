package game;

public class TwoPlayerGame {
    private final Board board;
    private final Player player1;
    private final Player player2;

    public TwoPlayerGame(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(boolean log) {
        while (true) {
            while (true) {
                final int result1 = makeMove(player1, 1, log);
                if (result1 == 3) {
                    board.makeTurnBack(player1.getFirst(), player1.getSecond());
                    board.makeTurnBack(player2.getFirst(), player2.getSecond());
                    continue;
                }
                if (result1 != -1) {
                    return result1;
                }
                break;
            }
            while (true) {
                final int result2 = makeMove(player2, 2, log);
                if (result2 == 3) {
                    System.out.println(player2.getFirst() + " " + player2.getSecond());
                    board.makeTurnBack(player2.getFirst(), player2.getSecond());
                    board.makeTurnBack(player1.getFirst(), player1.getSecond());
                    continue;
                }
                if (result2 != -1) {
                    return result2;
                }
                break;
            }
        }
    }

    private int makeMove(Player player, int no, boolean log) {
        final Move move = player.makeMove(board.getPosition());
        final GameResult result = board.makeMove(move);
        if (log) {
            System.out.println();
            System.out.println("Player: " + no);
            System.out.println(move);
            System.out.println(board);
            System.out.println("Result: " + result);
        }
        switch (result) {
            case RETURN://
                return 3;//
            case WIN:
                return no;
            case LOOSE:
                return 3 - no;
            case DRAW:
                return 0;
            case UNKNOWN:
                return -1;
            default:
                throw new AssertionError("Unknown makeMove result " + result);
        }
    }
}