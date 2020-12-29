package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.exception.ChessException;
import model.piecestate.PiecesStateListener;
import model.PieceType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a chess piece.
 */
public abstract class Piece implements PiecesStateListener {

    /**
     * Current location of the piece. Optional is empty if piece is not on the board.
     */
    protected Optional<Square> currentLocation;

    /**
     * All the squares the piece could move to.
     */
    protected Set<Square> moveableSquares;

    /**
     * Team the piece belongs to.
     */
    protected final Colour colour;

    /**
     * Type of the piece, eg. king, queen.
     */
    protected final PieceType type;

    /**
     * Constructs a piece at its starting location.
     * @param colour of the piece
     */
    protected Piece(Colour colour, Square startingSquare, PieceType type) {
        this.colour = colour;
        this.moveableSquares = new HashSet<>();
        currentLocation = Optional.of(startingSquare);
        this.type = type;
    }

    /**
     * Moves the piece to the given square.
     * @param square to move to
     * @throws ChessException if piece is dead
     * @throws ChessException if the piece is alive but not allowed to move to the square
     */
    public void moveTo(Square square) {
        if (isAlive()) {
            if (moveableSquares.contains(square)) {
                currentLocation = Optional.of(square);
                return;
            }
            throw new ChessException(String.format("Not allowed to move %s %s to square %s.", getColour(), getType(), square)); // TODO log
        }
        throw new ChessException(String.format("Not allowed to move %s %s as it is not alive.", getColour(), getType())); // TODO log
    }

    /**
     * Moves the piece to the given square without checking that the move is legal. If the piece is dead,
     * calling this method will resurrect the piece.
     * @param square
     */
    public void moveToUnchecked(Square square) {
        currentLocation = Optional.of(square);
    }

    /**
     * @param square
     * @return true if the piece can move to the square, false otherwise.
     */
    public boolean canMoveTo(Square square) {
        return moveableSquares.contains(square);
    }

    /**
     * @return the set of squares the piece can move to
     */
    public Set<Square> getMoveableSquares() {
        return moveableSquares;
    }

    /**
     * @return true if the piece is currently part of the game, ie. has not been taken.
     */
    public boolean isAlive() {
        return currentLocation.isPresent();
    }

    /**
     * @return the square the piece is currently on
     * @throws ChessException if the piece is not currently on a square, ie. has been taken
     */
    public Square getCurrentSquare() {
        if (isAlive()) {
            return currentLocation.get();
        }
        throw new RuntimeException(String.format("Cannot get location of %s %s as piece is dead", getColour(), getType())); // TODO log
    }

    /**
     * @return the team the piece belongs to
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Updates state of the piece based on new state of the board.
     * @param event containing new state of the board
     */
    @Override
    public void update(PiecesState event) {
        updateIsAlive(event);
        if (isAlive()) {
            updateMoveableSquares(event);
            return;
        }
        moveableSquares = new HashSet<>();
    }

    /**
     * Checks if piece is still alive based on the new state of the board.
     * @param event containing new state of the board
     */
    private void updateIsAlive(PiecesState event) {
        if (isAlive()) {
            // This piece has been removed from the board if an enemy piece is in its square
            if (event.getColourLocations().get(getCurrentSquare()) != colour) {
                currentLocation = Optional.empty();
            }
        }
        // TODO promotion?
    }

    /**
     * Updates set of squares piece can move to based on the new state of the board.
     * @param event containing new state of the board
     */
    protected abstract void updateMoveableSquares(PiecesState event);

    /**
     * @return type of this chess piece, eg. knight
     */
    public PieceType getType() {
        return type;
    }

}
