package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesUtil;
import model.PieceType;

public class Knight extends Piece {

    public Knight(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.KNIGHT);
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = MovesUtil.getMoveableSquaresForKnight(getCurrentSquare(), colour, event);
    }
}
