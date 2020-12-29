package model.exception;

/**
 * Exception class for when users have tried to make moves that are not allowed in chess
 */
public class ChessException extends RuntimeException {
    public ChessException(String msg) {
        super(msg);
    }
}
