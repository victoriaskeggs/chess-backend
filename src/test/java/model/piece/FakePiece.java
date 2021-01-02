package model.piece;

import model.Square;
import model.pieces.PiecesState;
import testutil.CollectionUtil;

/**
 * A fake Piece subclass for testing Piece
 */
public class FakePiece extends Piece {

    public FakePiece(PieceState pieceState) {
        super(pieceState);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        moveableSquares = CollectionUtil.createSet(new Square[] {Square.H2, Square.C7});
        threatenedSquares = moveableSquares;
    }
}
