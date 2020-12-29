package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesUtil;
import model.PieceType;

public class King extends Piece {

    public King(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.KING);
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = MovesUtil.getMoveableSquaresForKing(getCurrentSquare(), colour, event);
    }
}
