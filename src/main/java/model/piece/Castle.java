package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesUtil;
import model.PieceType;

public class Castle extends Piece {

    public Castle(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.CASTLE);
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = MovesUtil.getMoveableSquaresForCastle(getCurrentSquare(), colour, event);
    }
}
