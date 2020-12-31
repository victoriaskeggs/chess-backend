package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.exception.ChessException;
import model.piecestate.PiecesStateListener;
import model.PieceType;
import model.util.MovesCalculator;

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
        this.movesCalculator = new MovesCalculator();
        this.threatenedSquares = new HashSet<>();
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
            updateThreatenedAndMoveableSquares(event);
            return;
        }
        moveableSquares = new HashSet<>();
        threatenedSquares = new HashSet<>();
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
     * Updates set of squares piece can move to and threaten/protect based on the new state of the board.
     * @param event containing new state of the board
     */
    protected abstract void updateThreatenedAndMoveableSquares(PiecesState event);

    /**
     * @return type of this chess piece, eg. knight
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Checks if this Piece is equal to another object. They are equal if the other object is a Piece with
     * the same colour, type and current square as this Piece.
     * @param other
     * @return true if the piece is equal to other
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Piece) {
            Piece otherPiece = (Piece) other;
            if (otherPiece.colour == colour && otherPiece.type == type &&
                    otherPiece.currentLocation.equals(currentLocation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = colour.hashCode() * 5 + type.hashCode() * 7;
        if (currentLocation.isPresent()) {
            hash += + 11 * currentLocation.get().hashCode();
        }
        return hash;
    }
}
