package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;

/**
 * Immutable class representing the state of a Piece at a moment in time.
 */
public class PieceState {
    /**
     * Current location of the piece. Location is SQUARE.None if piece is not on the board.
     */
    private final Square square;

    /**
     * Team the piece belongs to.
     */
    private final Colour colour;

    /**
     * Type of the piece, eg. king, queen.
     */
    private final PieceType type;

    public PieceState(PieceType type, Colour colour, Square square) {
        this.type = type;
        this.colour = colour;
        this.square = square;
    }

    /**
     * Gets the current location of the piece. Location is SQUARE.None if piece is not on the board.
     */
    public Square getSquare() {
        return square;
    }

    /**
     * @return the team the piece belongs to.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @return the type of the piece, eg. KING
     */
    public PieceType getType() {
        return type;
    }

    /**
     * @return true if the piece is currently part of the game, ie. has not been taken.
     */
    public boolean isAlive() {
        return square != Square.NONE;
    }

    /**
     * Checks if this PieceState is equal to another object. They are equal if the other object is a PieceState with
     * the same colour, type and current square as this PieceState.
     * @param other to compare for equality against
     * @return true if the piece state is equal to other
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof PieceState) {
            PieceState otherState = (PieceState) other;
            if (otherState.colour == colour && otherState.type == type &&
                    otherState.square.equals(square)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return colour.hashCode() * 5 + type.hashCode() * 7 + 11 * square.hashCode();
    }
}
