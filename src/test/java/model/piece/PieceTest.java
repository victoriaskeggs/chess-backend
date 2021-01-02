package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;
import model.exception.ChessException;
import model.pieces.PiecesState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.CollectionUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PieceTest {

    /**
     * Piece subclass for testing Piece abstract class
     */
    private FakePiece fakePiece;

    /**
     * Sets up a fake white piece on square E1 that is allowed to move to squares B6 and A8
     */
    @BeforeEach
    public void setupFakePiece() {
        fakePiece = new FakePiece(new PieceState(PieceType.PAWN, Colour.WHITE, Square.E1));
        fakePiece.moveableSquares = CollectionUtil.createSet(new Square[] {Square. B6, Square.A8});;
    }

    @Test
    public void testMoveToWhenMoveAllowed() {
        // When
        fakePiece.moveTo(Square.B6);

        // Then
        assertEquals(Square.B6, fakePiece.getState().getSquare());
    }

    @Test
    public void testMoveToWhenMoveNotAllowed() {
        // When
        try {
            fakePiece.moveTo(Square.C5);
            fail();

            // Then
        } catch (ChessException exception) {
            assertEquals("Not allowed to move WHITE PAWN to square C5.", exception.getMessage());
        }
    }

    @Test
    public void testMoveToWhenDead() {
        // Given
        kill(fakePiece);

        // When
        try {
            fakePiece.moveTo(Square.A8);
            fail();

            // Then
        } catch (ChessException exception) {
            assertEquals("Not allowed to move WHITE PAWN as it is not alive.", exception.getMessage());
        }
    }

    @Test
    public void testMoveToUncheckedWhenMoveNotAllowed() {
        // When
        fakePiece.moveToUnchecked(Square.C5);

        // Then
        assertEquals(Square.C5, fakePiece.getState().getSquare());
    }

    @Test
    public void testMoveToUncheckedWhenDead() {
        // Given
        kill(fakePiece);

        // When
        fakePiece.moveToUnchecked(Square.A8);

        // Then
        assertEquals(Square.A8, fakePiece.getState().getSquare());
    }

    @Test
    public void testCanMoveToWhenMoveAllowed() {
        assertTrue(fakePiece.canMoveTo(Square.B6));
    }

    @Test
    public void testCanMoveToWhenMoveNotAllowed() {
        assertFalse(fakePiece.canMoveTo(Square.H8));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateWhenAlive() {
        // Given
        PiecesState state = mock(PiecesState.class);

        // When
        fakePiece.update(state); // fake piece updates moveable squares to H2 and C7

        // Then
        Set<Square> expected = CollectionUtil.createSet(new Square[] {Square.H2, Square.C7});
        assertEquals(expected, fakePiece.getMoveableSquares());
        assertTrue(fakePiece.getState().isAlive());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateWhenDead() {
        // Given
        PiecesState state = mock(PiecesState.class);
        kill(fakePiece);

        // When
        fakePiece.update(state);

        // Then
        assertFalse(fakePiece.getState().isAlive());
        assertEquals(new HashSet<>(), fakePiece.getMoveableSquares());
        assertEquals(new HashSet<>(), fakePiece.getThreatenedSquares());
    }

    private void kill(Piece piece) {
        piece.moveToUnchecked(Square.NONE);
    }
}
