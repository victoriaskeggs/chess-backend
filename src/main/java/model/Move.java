package model;

import model.piece.PieceState;

/**
 * Represents a chess move, where a piece moves from one square to another square.
 */
public class Move {

    private final PieceState pieceState;
    private final Square to;

    /**
     * Constructs a representation of a chess move
     * @param pieceState
     * @param to square the piece is moving to
     */
    public Move(PieceState pieceState, Square to) {
        this.pieceState = pieceState;
        this.to = to;
    }

    /**
     * @return state of piece making the move
     */
    public PieceState getPieceState() {
        return pieceState;
    }

    /**
     * @return square the piece is moving to
     */
    public Square getTo() {
        return to;
    }
}