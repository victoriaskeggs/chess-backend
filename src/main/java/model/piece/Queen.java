package model.piece;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import model.util.MovesCalculator;
import model.PieceType;

public class Queen extends Piece {

    public Queen(Colour colour, Square startingSquare) {
        super(colour, startingSquare, PieceType.QUEEN);
    }

    @Override
    protected void updateThreatenedAndMoveableSquares(PiecesState event) {
        movesCalculator.calculateMoveableAndThreatenedSquaresForQueen(getCurrentSquare(), colour, event);
        threatenedSquares = movesCalculator.getThreatenedSquares();
        moveableSquares = movesCalculator.getMoveableSquares();
        movesCalculator.resetThreatenedAndMoveableSquares();
    }
}
