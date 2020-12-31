package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesCalculator;
import model.PieceType;

public class King extends Piece {

    public King(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.KING);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForKing(getCurrentSquare(), colour, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.resetThreatenedAndMoveableSquares();
    }
}
