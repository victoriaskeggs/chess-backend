package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import model.pieces.PiecesState;

public class Queen extends Piece {

    public Queen(Colour colour, Square startingSquare) {
        super(new PieceState(PieceType.QUEEN, colour, startingSquare));
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForQueen(state, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.reset();
    }
}
