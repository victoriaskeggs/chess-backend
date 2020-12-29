package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesUtil;
import model.PieceType;

public class Bishop extends Piece {

    public Bishop(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.BISHOP);
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = MovesUtil.getMoveableSquaresForBishop(getCurrentSquare(), colour, event);
    }
}
