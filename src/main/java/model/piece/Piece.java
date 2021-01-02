package model.piece;

import model.Square;
import model.exception.ChessException;
import model.listener.PiecesStateListener;
import model.pieces.PiecesState;
import model.util.MovesCalculator;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a chess piece.
 */
public abstract class Piece implements PiecesStateListener {
    /**
     * Calculates threatened and moveable squares
     */
    protected MovesCalculator movesCalculator;

    /**
     * All the squares the piece could move to.
     */
    protected Set<Square> moveableSquares;

    /**
     * All the squares the piece can threaten/protect.
     */
    protected Set<Square> threatenedSquares;

    /**
     * Current state of the piece.
     */
    protected PieceState state;

    /**
     * Constructs a piece in a given state
     * @param pieceState
     */
    protected Piece(PieceState pieceState) {
        this.state = pieceState;
        this.movesCalculator = new MovesCalculator();
        this.threatenedSquares = new HashSet<>();
        this.moveableSquares = new HashSet<>();
    }

    /**
     * Moves the piece to the given square.
     * @param square to move to
     * @throws ChessException if piece is dead
     * @throws ChessException if the piece is alive but not allowed to move to the square
     */
    public void moveTo(Square square) {
        if (state.isAlive()) {
            if (moveableSquares.contains(square)) {
                moveToUnchecked(square);
                return;
            }
            throw new ChessException(String.format("Not allowed to move %s %s to square %s.", state.getColour(), state.getType(), square)); // TODO log
        }
        throw new ChessException(String.format("Not allowed to move %s %s as it is not alive.", state.getColour(), state.getType())); // TODO log
    }

    /**
     * Moves the piece to the given square without checking that the move is legal. If the piece is dead,
     * calling this method will resurrect the piece.
     * @param square
     */
    public void moveToUnchecked(Square square) {
        state = new PieceState(state.getType(), state.getColour(), square);
    }

    /**
     * @param square
     * @return true if the piece can move to the square, false otherwise.
     */
    public boolean canMoveTo(Square square) {
        return moveableSquares.contains(square);
    }

    /**
     * @param square
     * @return true if the piece threatens/protects the square, false otherwise
     */
    public boolean doesThreaten(Square square) {
        return threatenedSquares.contains(square);
    }

    /**
     * @return the set of squares the piece can move to
     */
    public Set<Square> getMoveableSquares() {
        return moveableSquares;
    }

    /**
     * @return the set of squares the piece threatens
     */
    public Set<Square> getThreatenedSquares() {
        return threatenedSquares;
    }

    /**
     * @return the current state of the piece
     */
    public PieceState getState() {
        return state;
    }

    /**
     * Updates state of the piece based on new state of the board.
     * @param state containing new state of the board
     */
    @Override
    public void update(PiecesState state) {
        if (this.state.isAlive()) {
            updateThreatenedAndMoveableSquares(state);
            return;
        }
        moveableSquares = new HashSet<>();
        threatenedSquares = new HashSet<>();
    }

    /**
     * Updates set of squares piece can move to and threaten/protect based on the new state of the board.
     * @param event containing new state of the board
     */
    protected abstract void updateThreatenedAndMoveableSquares(PiecesState event);

    @Override
    public boolean equals(Object other) {
        if (other instanceof Piece) {
            return state.equals(((Piece) other).state);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}
