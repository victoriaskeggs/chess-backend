package model;

/**
 * Represents a chess move, where a piece moves from one square to another square.
 */
public class Move {

    private final PieceType pieceType;
    private final Colour colour;
    private final Square from;
    private final Square to;

    /**
     * Constructs a representation of a chess move
     * @param pieceType type of piece making the move
     * @param colour of piece making the move
     * @param from square the piece is moving from
     * @param to square the piece is moving to
     */
    public Move(PieceType pieceType, Colour colour, Square from, Square to) {
        this.pieceType = pieceType;
        this.colour = colour;
        this.from = from;
        this.to = to;
    }

    /**
     * @return type of piece making the move
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * @return colour of piece making the move
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @return square the piece is moving from
     */
    public Square getFrom() {
        return from;
    }

    /**
     * @return square the piece is moving to
     */
    public Square getTo() {
        return to;
    }
}
