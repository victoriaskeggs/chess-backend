package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import model.pieces.PiecesState;

public class King extends Piece {

    public King(Colour colour, Square startingSquare) {
        super(new PieceState(PieceType.KING, colour, startingSquare));
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForKing(state, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.reset();
    }
}
