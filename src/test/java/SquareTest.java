import model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SquareTest {
    @Test
    public void testByPositionGivesCorrectRowAndLetterNumbers() {
        for (int rowNumber = 1; rowNumber <= 8; rowNumber++) {
            for (int letterNumber = 1; letterNumber <=8; letterNumber++) {
                // When
                Square square = Square.byPosition(rowNumber, letterNumber);

                // Then
                assertEquals(rowNumber, square.getRowNumber());
                assertEquals(letterNumber, square.getLetterNumber());
            }
        }
    }

    @Test
    public void testByPositionGivesCorrectEnumValue() {
        // When
        Square actual = Square.byPosition(5, 2);

        // Then
        assertEquals(Square.B5, actual);
    }

    @Test
    public void testByPositionWhenRowNumberOutOfRangeThrowsException() {
        try {
            Square.byPosition(9, 2);
            fail();
        } catch (RuntimeException exception) {
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(exception.getMessage(), "No square at row number 9 and letter number 2.");
        }
    }

    @Test
    public void testByPositionWhenLetterNumberOutOfRangeThrowsException() {
        try {
            Square.byPosition(7, 9);
            fail();
        } catch (RuntimeException exception) {
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(exception.getMessage(), "No square at row number 7 and letter number 9.");
        }
    }
}
