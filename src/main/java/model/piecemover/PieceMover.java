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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PieceMover {

    /**
     * Pieces this piece mover can move
     */
    protected Map<PieceType, Set<Piece>> piecesByType;

    /**
     * Objects interested in PiecesStateEvents.
     */
    protected Set<PiecesStateListener> piecesStateListeners;

    /**
     * Sets up a PieceMover without control over any pieces and without any registered PiecesUpdateListeners
     */
    public PieceMover() {
        piecesByType = new HashMap<>();
        piecesStateListeners = new HashSet<>();
    }

    /**
     * Sets up all the pieces controlled by this PieceMover as PiecesStateListeners
     * @return constructed set of PiecesUpdateListeners
     */
    protected void setupPiecesAsPiecesStateListeners() {
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

        // Update other pieces update listeners' view
        firePiecesStateEvent();
    }

    /**
     * Lets listeners know that the state of the pieces has changed.
     */
    protected void firePiecesStateEvent() {
        // Construct map of squares to colour of piece on the square
        Map<Square, Colour> colourLocations = new HashMap<>();
        for (Piece piece : CollectionUtil.combine(piecesByType.values())) {
            if (piece.isAlive()) {
                colourLocations.put(piece.getCurrentSquare(), piece.getColour());
            }
        }

        // Construct set of squares which contain kings
        Set<Square> kingLocations = new HashSet<>();
        for (Piece king : piecesByType.get(PieceType.KING)) {
            kingLocations.add(king.getCurrentSquare());
        }

        // Fire pieces update event to all listeners
        PiecesState event = new PiecesState(colourLocations, kingLocations);
        for (PiecesStateListener listener : piecesStateListeners) {
            listener.update(event);
        }
    }

    /**
     * Finds the living piece of the given colour and type at a specified location.
     * @param type
     * @param colour
     * @param location
     * @throws ChessException if there is no living piece at the specified location of the given colour and type
     * @return
     */
    protected Piece findPiece(PieceType type, Colour colour, Square location) {
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
    protected Set<Piece> findPieces(PieceType type, Colour colour) {
        Set<Piece> pieces = new HashSet<>();
        for (Piece piece : piecesByType.get(type)) {
            if (piece.isAlive() && piece.getColour() == colour) {
                pieces.add(piece);
            }
        }
        return pieces;
    }
}
