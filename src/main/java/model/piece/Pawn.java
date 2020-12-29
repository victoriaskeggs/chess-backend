package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesUtil;
import model.PieceType;

public class Pawn extends Piece {

    public Pawn(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.PAWN);
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = MovesUtil.getMoveableSquaresForPawn(getCurrentSquare(), colour, event);
    }
}
