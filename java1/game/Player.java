package game;

public interface Player {
    Move makeMove(Position position);
    int getFirst();
    int getSecond();
}