package testutil;
import model.Colour;
import model.PieceType;
import model.Square;
import model.piece.Piece;
import model.piecestate.PiecesState;

import java.util.Optional;
import java.util.Set;

/**
 * A fake Piece subclass for testing Piece
 */
public class FakePiece extends Piece {

    public FakePiece(Colour colour, Square square, PieceType type) {
        super(colour, square, type);
    }

    /**
     * Sets the squares the piece can move to
     * @param squares
     */
    public void setMoveableSquares(Set<Square> squares) {
        moveableSquares = squares;
    }

    /**
     * Sets the piece as dead
     */
    public void kill() {
        currentLocation = Optional.empty();
    }

    @Override
    protected void updateMoveableSquares(PiecesState event) {
        moveableSquares = CollectionUtil.createSet(new Square[] {Square.H2, Square.C7});
    }
}
