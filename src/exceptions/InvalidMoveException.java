package exceptions;

public class InvalidMoveException extends RuntimeException{

    public InvalidMoveException(String message) {
        super("Déplacement interdit: " + message);
    }
}
