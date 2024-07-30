package game;

public interface Board {
    Position getPosition();

    GameResult makeMove(Move move);

    void makeTurnBack(int x, int y);
}