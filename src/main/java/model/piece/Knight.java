package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesCalculator;
import model.PieceType;

public class Knight extends Piece {

    public Knight(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.KNIGHT);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForKnight(getCurrentSquare(), colour, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.resetThreatenedAndMoveableSquares();
    }
}
