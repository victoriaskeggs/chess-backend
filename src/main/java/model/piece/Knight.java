package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import model.pieces.PiecesState;

public class Knight extends Piece {

    public Knight(Colour colour, Square startingSquare) {
        super(new PieceState(PieceType.KNIGHT, colour, startingSquare));
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForKnight(state, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.reset();
    }
}
