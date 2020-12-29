package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;
import model.exception.ChessException;
import model.piecestate.PiecesState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import testutil.CollectionUtil;
import testutil.FakePiece;

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
        fakePiece = new FakePiece(Colour.WHITE, Square.E1, PieceType.PAWN);
        Set<Square> moveableSquares = CollectionUtil.createSet(new Square[] {Square. B6, Square.A8});
        fakePiece.setMoveableSquares(moveableSquares);
    }

    @Test
    public void testMoveToWhenMoveAllowed() {
        // When
        fakePiece.moveTo(Square.B6);

        // Then
        assertEquals(Square.B6, fakePiece.getCurrentSquare());
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
        fakePiece.kill();

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
        assertEquals(Square.C5, fakePiece.getCurrentSquare());
    }

    @Test
    public void testMoveToUncheckedWhenDead() {
        // Given
        fakePiece.kill();

        // When
        fakePiece.moveToUnchecked(Square.A8);

        // Then
        assertEquals(Square.A8, fakePiece.getCurrentSquare());
    }

    @Test
    public void testCanMoveToWhenMoveAllowed() {
        assertTrue(fakePiece.canMoveTo(Square.B6));
    }

    @Test
    public void testCanMoveToWhenMoveNotAllowed() {
        assertFalse(fakePiece.canMoveTo(Square.H8));
    }

    @Test
    public void testIsAliveWhenAlive() {
        assertTrue(fakePiece.isAlive());
    }

    @Test
    public void testIsAliveWhenDead() {
        // Given
        fakePiece.kill();

        // When and then
        assertFalse(fakePiece.isAlive());
    }

    @Test
    public void testIsAliveWhenResurrected() {
        // Given
        fakePiece.kill();

        // When
        fakePiece.moveToUnchecked(Square.A8);

        // Then
        assertTrue(fakePiece.isAlive());
    }

    @Test
    public void testGetCurrentSquareWhenAlive() {
        assertEquals(Square.E1, fakePiece.getCurrentSquare());
    }

    @Test
    public void testGetCurrentSquareWhenDead() {
        // Given
        fakePiece.kill();

        // When
        try {
            fakePiece.getCurrentSquare();
            fail();

            // Then
        } catch (RuntimeException exception) {
            assertEquals(RuntimeException.class, exception.getClass());
            assertEquals("Cannot get location of WHITE PAWN as piece is dead", exception.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateWhenNotKilled() {
        // Given
        PiecesState state = mock(PiecesState.class);
        when(state.getColourLocations()).thenAnswer((Answer<HashMap<Square, Colour>>) invocation -> {
            HashMap<Square, Colour> colourLocations = mock(HashMap.class);
            when(colourLocations.get(Square.E1)).thenReturn(Colour.WHITE); // non-enemy piece on current square
            return colourLocations;
        });

        // When
        fakePiece.update(state); // fake piece updates moveable squares to H2 and C7

        // Then
        Set<Square> expected = CollectionUtil.createSet(new Square[] {Square.H2, Square.C7});
        assertEquals(expected, fakePiece.getMoveableSquares());
        assertTrue(fakePiece.isAlive());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateWhenKilled() {
        // Given
        PiecesState state = mock(PiecesState.class);
        when(state.getColourLocations()).thenAnswer((Answer<HashMap<Square, Colour>>) invocation -> {
            HashMap<Square, Colour> colourLocations = mock(HashMap.class);
            when(colourLocations.get(Square.E1)).thenReturn(Colour.BLACK); // enemy piece on current square
            return colourLocations;
        });

        // When
        fakePiece.update(state);

        // Then
        assertFalse(fakePiece.isAlive());
        assertEquals(new HashSet<>(), fakePiece.getMoveableSquares());
    }
}
