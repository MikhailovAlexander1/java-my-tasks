package game;

public class Move  {
    private int row;
    private int col;
    private Cell value;

    public Move(int row, int col, Cell value) {
        try {
            this.row = row;
            this.col = col;
            this.value = value;
        } catch (Exception any) {
            System.out.println("Incorrect move!!");
            this.row = -5;
            this.col = -5;
            this.value = value;
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Cell getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Move(%s, %d, %d)", value, row + 1, col + 1);
    }
}