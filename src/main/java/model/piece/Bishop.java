package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.PieceType;

public class Bishop extends Piece {

    public Bishop(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.BISHOP);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForBishop(getCurrentSquare(), colour, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.resetThreatenedAndMoveableSquares();
    }
}
