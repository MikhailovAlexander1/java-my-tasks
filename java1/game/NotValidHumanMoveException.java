package game;

public class NotValidHumanMoveException extends Exception {
    public NotValidHumanMoveException (String message) {
        super(message);
    }
}
