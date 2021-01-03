package model.pieces;

import model.Colour;
import model.Move;
import model.Square;
import model.exception.ChessException;
import model.listener.PiecesStateListener;
import model.piece.Piece;
import model.PieceType;
import model.piece.PieceFactory;
import model.piece.PieceState;
import util.CollectionUtil;

import java.util.*;

public class PiecesMover {

    /**
     * Moves made on previous turn
     */
    private Set<Move> previousMoves;

    /**
     * Pieces this piece mover can move, mapped by their type
     */
    private final Map<PieceType, Set<Piece>> piecesByType;

    /**
     * Objects interested in PiecesStateEvents.
     */
    private final Set<PiecesStateListener> piecesStateListeners;

    /**
     * Factory to generate new Pieces.
     */
    private PieceFactory pieceFactory;

    /**
     * Sets up a PieceMover with control over new pieces that start in the provided states.
     */
    public PiecesMover(PiecesState piecesState) {
        this();
        addPieces(piecesState);
    }

    /**
     * Sets up a PieceMover with no control over any pieces.
     */
    public PiecesMover() {
        previousMoves = new HashSet<>();
        piecesByType = new HashMap<>();
        piecesStateListeners = new HashSet<>();
        pieceFactory = new PieceFactory();
    }

    /**
     * Creates new Pieces in the provided states and places them under the control of this PieceMover
     * @param piecesState
     */
    public void addPieces(PiecesState piecesState) {
        for (PieceState state : piecesState.getPieceStates()) {
            // Create new Piece in the given state
            Piece piece = pieceFactory.createPiece(state);

            // Map Piece by its type
            CollectionUtil.addToMap(piecesByType, state.getType(), piece);

            // Add Piece as a listener to this PiecesMover's state
            piecesStateListeners.add(piece);
        }
        firePiecesStateUpdate();
    }

    /**
     * Removes control of this PieceMover over all its Pieces
     */
    public void clearPieces() {
        piecesByType.clear();
        piecesStateListeners.clear();
    }

    /**
     * Moves a piece to a new square if allowed.
     * @param move
     */
    public void move(Move move) {
        // Update square piece is on
        Piece piece = findPiece(move.getPieceState());
        piece.moveTo(move.getTo());
        previousMoves.add(move);

        // Take any pieces on this square off the board
        ensureOnlyPieceIsOnSquare(piece, move.getTo());

        // Update other listeners' view
        firePiecesStateUpdate();
    }

    /**
     * Reverses the moves made on the previous turn. Two or more moves cannot be undone in a row.
     */
    public void undoMove() {
        // Check if there are moves to be reversed
        if (previousMoves.isEmpty()) {
            throw new RuntimeException(); // TODO log and msg - cannot undo move
        }
        // Reverse the moves that occured on the previous turn
        for (Move move : previousMoves) {
            // Retrieve square piece was on before moved
            Square previousSquare = move.getPieceState().getSquare();
            Piece piece = findPiece(move.getPieceState().getType(), move.getPieceState().getColour(), move.getTo());
            piece.moveToUnchecked(previousSquare);

            // Update other listeners' view
            firePiecesStateUpdate();
        }
        previousMoves.clear();
    }

    /**
     * Ensures only the provided piece is on the given square. Takes any other pieces on this square
     * off the board and adds these moves to previousMoves.
     * @param piece
     * @param square
     */
    private void ensureOnlyPieceIsOnSquare(Piece piece, Square square) {
        Set<Piece> piecesOnSquare = findPieces(square);
        piecesOnSquare.remove(piece);
        for (Piece otherPiece : piecesOnSquare) {
            Move move = new Move(otherPiece.getState(), Square.NONE);
            otherPiece.moveToUnchecked(move.getTo());
            previousMoves.add(move);
        }
    }

    /**
     * Finds all the pieces on a given square.
     * @param square
     * @return pieces on the square
     */
    private Set<Piece> findPieces(Square square) {
        Set<Piece> piecesOnSquare = new HashSet<>();
        Set<Piece> allPieces = CollectionUtil.combine(piecesByType.values());
        for (Piece piece : allPieces) {
            if (piece.getState().getSquare() == square) {
                piecesOnSquare.add(piece);
            }
        }
        return piecesOnSquare;
    }

    /**
     * Lets listeners know that the state of the pieces has changed.
     */
    private void firePiecesStateUpdate() {
        PiecesState state = generatePiecesState();
        for (PiecesStateListener listener : piecesStateListeners) {
            listener.update(state);
        }
    }

    /**
     * @return an immutable representation of the state of all the pieces under this PieceMover's control
     */
    public PiecesState generatePiecesState() {
        Set<PieceState> pieceStates = new HashSet<>();
        Set<Piece> pieces = CollectionUtil.combine(piecesByType.values());
        for (Piece piece : pieces) {
            pieceStates.add(piece.getState());
        }
        return new PiecesState(pieceStates);
    }

    /**
     * Finds the piece of the given colour and type at a specified location.
     * @param type
     * @param colour
     * @param location
     * @throws ChessException if there is no piece at the specified location of the given colour and type
     * @return
     */
    public Piece findPiece(PieceType type, Colour colour, Square location) {
        return findPiece(new PieceState(type, colour, location));
    }

    /**
     * Finds the piece of the given colour and type at a specified location.
     * @param state
     * @throws ChessException if there is no piece in the specified state
     * @return
     */
    public Piece findPiece(PieceState state) {
        Set<Piece> candidates = findPieces(state.getType(), state.getColour());
        for (Piece candidate : candidates) {
            if (candidate.getState().getSquare() == state.getSquare()) {
                return candidate;
            }
        }
        throw new ChessException(String.format("There is no %s", state)); // TODO log
    }

    /**
     * Finds all pieces under this mover's control (including dead pieces) of the specified type and colour.
     * @param type
     * @param colour
     * @return set of pieces
     */
    public Set<Piece> findPieces(PieceType type, Colour colour) {
        Set<Piece> pieces = new HashSet<>();
        for (Piece piece : piecesByType.get(type)) {
            if (piece.getState().getColour() == colour) {
                pieces.add(piece);
            }
        }
        return pieces;
    }

    /**
     * @return all pieces under control of this PiecesMover
     */
    public Set<Piece> getPieces() {
        return CollectionUtil.combine(piecesByType.values());
    }
}
