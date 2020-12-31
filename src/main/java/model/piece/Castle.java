package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesCalculator;
import model.PieceType;

public class Castle extends Piece {

    public Castle(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.CASTLE);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForCastle(getCurrentSquare(), colour, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.resetThreatenedAndMoveableSquares();
    }
}
