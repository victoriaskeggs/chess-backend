package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;
import model.piece.Piece;
import model.piecestate.PiecesState;
import testutil.CollectionUtil;

/**
 * A fake Piece subclass for testing Piece
 */
public class FakePiece extends Piece {

    public FakePiece(Colour colour, Square square, PieceType type) {
        super(colour, square, type);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        moveableSquares = CollectionUtil.createSet(new Square[] {Square.H2, Square.C7});
        threatenedSquares = moveableSquares;
    }
}