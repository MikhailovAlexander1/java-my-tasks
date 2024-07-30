package game;

import java.util.Arrays;
import java.util.Map;

public class TicTacToeBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );

    private final String[][] field;
    private Cell turn;
    private final int m;
    private final int n;
    private final int k;

    public TicTacToeBoard(int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        field = new String[m][n];
        for (String[] row : field) {
            Arrays.fill(row, CELL_TO_STRING.get(Cell.E));
        }
        turn = Cell.X;
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!isValid(move)) {
            if (move.getRow() == 0 && move.getCol() == 0 && move.getValue() != turn) {
                return GameResult.RETURN;
            }
            return GameResult.LOOSE;
        }

        field[move.getRow()][move.getCol()] = CELL_TO_STRING.get(move.getValue());
        if (checkWin()) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return GameResult.UNKNOWN;
    }

    public void makeTurnBack(int x, int y) {
        field[x][y] = CELL_TO_STRING.get(Cell.E);
    }

    private boolean checkDraw() {
        int count = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (field[r][c].equals(CELL_TO_STRING.get(Cell.E))) {
                    count++;
                }
            }
        }
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWin() {
        int count = 0;
        for (int r = 0; r < m; r++) {
            count = 0;
            for (int c = 0; c < n; c++) {
                if (field[r][c].equals(CELL_TO_STRING.get(turn))) {
                    count++;
                } else if (count != 0) {
                    break;
                }
            }
            if (count == k) {
                return true;
            }
        }
        for (int c = 0; c < n; c++) {
            count = 0;
            for (int r = 0; r < m; r++) {
                if (field[r][c].equals(CELL_TO_STRING.get(turn))) {
                    count++;
                } else if (count != 0) {
                    break;
                }
            }
            if (count == k) {
                return true;
            }
        }
        if (m >= k && n >= k) {
            count = 0;
            for (int i = k - 1; i < n; i++) {
                int y = 0;
                for (int j = i; j >= 0; j--) {
                    if (field[y][j].equals(CELL_TO_STRING.get(turn))) {
                        count++;
                    } else if (count == k) {
                        return true;
                    } else if (count != 0) {
                        count = 0;
                    }
                    y++;
                    if (y > m - 1) {
                        break;
                    }
                }
                if (count >= k) {
                    return true;
                }
            }
            if (checkerDiag(m - k, m, n - 1, k)) {
                return true;
            }
            if (checkerDiag(n - k, n,0, k)) {
                return true;
            }
            if (checkerDiag(m - k, m, 0, k)) {
                return true;
            }
        }
        return count >= k;
    }

    private boolean checkerDiag(int fromI, int jTo, int fromY, int k) {
        int count = 0;
        for (int i = fromI; i >= 0; i--) {
            int y = fromY;
            for (int j = i; j < jTo; j++) {
                if (field[j][y].equals(CELL_TO_STRING.get(turn))) {
                    count++;
                } else if (count == k){
                    return true;
                } else if (count != 0) {
                    count = 0;
                }
                y++;
                if (y >= n) {
                    break;
                }
            }
        }
        return count >= k;
    }

    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getCol() && move.getCol() < n
                && field[move.getRow()][move.getCol()].equals(CELL_TO_STRING.get(Cell.E))
                && turn == move.getValue();
    }

    @Override
    public Cell getCell(int row, int column) {
        return field[row][column].equals(CELL_TO_STRING.get(Cell.X)) ? Cell.X : Cell.O;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                sb.append(field[r][c]);
            }
            sb.append(System.lineSeparator());
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }
}