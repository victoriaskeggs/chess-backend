package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesUtil;
import model.PieceType;

public class Queen extends Piece {

    public Queen(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.QUEEN);
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = MovesUtil.getMoveableSquaresForQueen(getCurrentSquare(), colour, event);
    }
}
