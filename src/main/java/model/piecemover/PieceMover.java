package model.piecemover;

import model.Colour;
import model.Move;
import model.Square;
import model.piecestate.PiecesState;
import model.exception.ChessException;
import model.piecestate.PiecesStateListener;
import model.piece.Piece;
import model.PieceType;
import util.CollectionUtil;

import java.util.*;

public class PieceMover {

    /**
     * Previous move made
     */
    private Optional<Move> previousMove;

    /**
     * Pieces this piece mover can move
     */
    private final Map<PieceType, Set<Piece>> piecesByType;

    /**
     * Objects interested in PiecesStateEvents.
     */
    private final Set<PiecesStateListener> piecesStateListeners;

    /**
     * Sets up a PieceMover with control over the provided pieces.
     */
    public PieceMover(Map<PieceType, Set<Piece>> piecesByType) {
        previousMove = Optional.empty();
        this.piecesByType = piecesByType;
        this.piecesStateListeners = new HashSet<>();
        piecesStateListeners.addAll(CollectionUtil.combine(piecesByType.values()));
        firePiecesStateEvent();
    }

    /**
     * Moves a piece to a new square if allowed.
     * @param move
     */
    public void move(Move move) {
        // Update square piece is on
        Piece piece = findPiece(move.getPieceType(), move.getColour(), move.getFrom());
        piece.moveTo(move.getTo());
        previousMove = Optional.of(move);

        // Update other pieces update listeners' view
        firePiecesStateEvent();
    }

    /**
     * Reverses the previous move. Two or more moves cannot be undone in a row.
     */
    public void undoMove() {
        if (previousMove.isPresent()) {
            Piece piece = findPiece(previousMove.get().getPieceType(), previousMove.get().getColour(), previousMove.get().getTo());
            piece.moveToUnchecked(previousMove.get().getFrom());
            firePiecesStateEvent();
            previousMove = Optional.empty();
            return;
        }
        throw new RuntimeException(); // TODO log and msg - cannot undo move
    }

    /**
     * Lets listeners know that the state of the pieces has changed.
     */
    private void firePiecesStateEvent() {
        // Construct map of squares to colour of piece on the square
        Map<Square, Colour> colourLocations = new HashMap<>();
        for (Piece piece : CollectionUtil.combine(piecesByType.values())) {
            if (piece.isAlive()) {
                Square current = piece.getCurrentSquare();
                colourLocations.put(current, piece.getColour());
            }
        }

        // Construct set of squares which contain kings
        Set<Square> kingLocations = new HashSet<>();
        if (piecesByType.containsKey(PieceType.KING)) {
            for (Piece king : piecesByType.get(PieceType.KING)) {
                kingLocations.add(king.getCurrentSquare());
            }
        }

        // Fire pieces update event to all listeners
        PiecesState event = new PiecesState(colourLocations, kingLocations);
        for (PiecesStateListener listener : piecesStateListeners) {
            listener.update(event);
        }
    }

    /**
     * @return all the pieces under this PieceMover's control
     */
    public Set<Piece> getPieces() {
        return CollectionUtil.combine(piecesByType.values());
    }

    /**
     * Finds the living piece of the given colour and type at a specified location.
     * @param type
     * @param colour
     * @param location
     * @throws ChessException if there is no living piece at the specified location of the given colour and type
     * @return
     */
    public Piece findPiece(PieceType type, Colour colour, Square location) {
        Set<Piece> candidates = findPieces(type, colour);
        for (Piece candidate : candidates) {
            if (candidate.isAlive() && candidate.getCurrentSquare() == location) {
                return candidate;
            }
        }
        throw new ChessException(String.format("There is no %s %s at square %s", colour, type, location)); // TODO log
    }

    /**
     * Finds all pieces under this mover's control (which are still alive) of the specified type and colour.
     * @param type
     * @param colour
     * @return set of pieces
     */
    public Set<Piece> findPieces(PieceType type, Colour colour) {
        Set<Piece> pieces = new HashSet<>();
        for (Piece piece : piecesByType.get(type)) {
            if (piece.isAlive() && piece.getColour() == colour) {
                pieces.add(piece);
            }
        }
        return pieces;
    }
}
