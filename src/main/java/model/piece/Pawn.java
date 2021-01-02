package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import model.pieces.PiecesState;

public class Pawn extends Piece {

    public Pawn(Colour colour, Square startingSquare) {
        super(new PieceState(PieceType.PAWN, colour, startingSquare));
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForPawn(state, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.reset();
    }
}
