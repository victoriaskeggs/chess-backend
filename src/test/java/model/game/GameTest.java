package model.game;

import model.Colour;
import model.Move;
import model.PieceType;
import model.Square;
import model.exception.ChessException;
import model.pieces.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

public class GameTest {

    @Mock(name = "board")
    private Board board;

    private Move move;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        move = new Move(PieceType.KNIGHT, Colour.WHITE, Square.B8, Square.A6);
    }

    @Test
    public void testMoveWhenNotColoursTurn() {
        // Given
        Game game = new Game(GameState.IN_PROGRESS, board, Colour.BLACK);

        // When
        try {
            game.move(move);
            fail();
        } catch (ChessException exception) {

            // Then
            assertEquals("Colour WHITE cannot move as it is not their turn.", exception.getMessage());
        }
    }

    @Test
    public void testMoveWhenGameAlreadyOver() {
        // Given
        Game game = new Game(GameState.OVER_CHECKMATE, board, Colour.WHITE);

        // When
        try {
            game.move(move);
            fail();
        } catch (ChessException exception) {

            // Then
            assertEquals("Cannot make a move as game is over.", exception.getMessage());
        }
    }

    @Test
    public void testMoveWhenColourPuttingThemselvesIntoCheck() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(true);
        Game game = new Game(GameState.IN_PROGRESS, board, Colour.WHITE);

        // When
        try {
            game.move(move);
            fail();
        } catch (ChessException exception) {

            // Then
            assertEquals("Colour WHITE cannot be in check after their move.", exception.getMessage());
        }
    }

    @Test
    public void testMoveWhenMoveDoesNotResultInCheck() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isChecked(Colour.BLACK)).thenReturn(false);
        Game game = new Game(GameState.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(GameState.IN_PROGRESS, state);
    }

    @Test
    public void testMoveWhenMoveResultsInCheck() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isChecked(Colour.BLACK)).thenReturn(true);
        Game game = new Game(GameState.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(GameState.IN_PROGRESS_CHECK, state);
    }

    @Test
    public void testMoveWhenMoveResultsInCheckmate() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isCheckmated(Colour.BLACK)).thenReturn(true);
        Game game = new Game(GameState.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(GameState.OVER_CHECKMATE, state);
    }

    @Test
    public void testMoveWhenMoveResultsInStalemate() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isStalemated(Colour.BLACK)).thenReturn(true);
        Game game = new Game(GameState.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(GameState.OVER_STALEMATE, state);
    }
}
