package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import model.pieces.PiecesState;

public class Bishop extends Piece {

    public Bishop(Colour colour, Square startingSquare) {
        super(new PieceState(PieceType.BISHOP, colour, startingSquare));
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForBishop(state, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.reset();
    }
}
