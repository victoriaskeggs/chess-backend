package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceStateTest {
    @Test
    public void testEqualsWhenNotEqual() {
        // Given
        PieceState state1 = new PieceState(PieceType.QUEEN, Colour.WHITE, Square.A2);
        PieceState state2 = new PieceState(PieceType.QUEEN, Colour.WHITE, Square.A4);

        // When
        boolean areEqual = state1.equals(state2);
        int state1Code = state1.hashCode();
        int state2Code = state2.hashCode();

        // Then
        assertNotEquals(state1Code, state2Code);
        assertFalse(areEqual);
    }

    @Test
    public void testEqualsWhenEqual() {
        // Given
        PieceState state1 = new PieceState(PieceType.QUEEN, Colour.WHITE, Square.A2);
        PieceState state2 = new PieceState(PieceType.QUEEN, Colour.WHITE, Square.A2);

        // When
        boolean areEqual = state1.equals(state2);
        int state1Code = state1.hashCode();
        int state2Code = state2.hashCode();

        // Then
        assertEquals(state1Code, state2Code);
        assertTrue(areEqual);
    }
}
