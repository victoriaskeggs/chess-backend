package model.pieces;

import model.piece.PieceState;

import java.util.Collections;
import java.util.Set;

/**
 * Immutable class representing the state of a set of related Pieces.
 */
public class PiecesState {

    /**
     * Immutable set of piece states in this PiecesState
     */
    private final Set<PieceState> pieceStates;

    public PiecesState(Set<PieceState> pieceStates) {
        this.pieceStates = Collections.unmodifiableSet(pieceStates);
    }

    /**
     * @return immutable set of piece states in this PiecesState
     */
    public Set<PieceState> getPieceStates() {
        return pieceStates;
    }
}
