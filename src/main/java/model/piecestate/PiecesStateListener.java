package model.piecestate;

public interface PiecesStateListener {

    /**
     * Called every time the state of a collection of pieces changes.
     * @param event containing the new state of the collection of pieces
     */
    void update(PiecesState event);
}
