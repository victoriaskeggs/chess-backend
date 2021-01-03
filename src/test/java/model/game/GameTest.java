package model.game;

import model.Colour;
import model.Move;
import model.PieceType;
import model.Square;
import model.exception.ChessException;
import model.piece.PieceState;
import model.pieces.Board;
import model.pieces.PiecesState;
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

    @Mock
    private PiecesState mockPiecesState;

    private Move move;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        move = new Move(new PieceState(PieceType.KNIGHT, Colour.WHITE, Square.B8), Square.A6);
        when(board.move(move)).thenReturn(mockPiecesState);
    }

    @Test
    public void testMoveWhenNotColoursTurn() {
        // Given
        Game game = new Game(GameStatus.IN_PROGRESS, board, Colour.BLACK);

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
        Game game = new Game(GameStatus.OVER_CHECKMATE, board, Colour.WHITE);

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
        Game game = new Game(GameStatus.IN_PROGRESS, board, Colour.WHITE);

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
        Game game = new Game(GameStatus.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(GameStatus.IN_PROGRESS, state.getStatus());
    }

    @Test
    public void testMoveWhenMoveResultsInCheck() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isChecked(Colour.BLACK)).thenReturn(true);
        Game game = new Game(GameStatus.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(mockPiecesState, state.getState());
        assertEquals(GameStatus.IN_PROGRESS_CHECK, state.getStatus());
    }

    @Test
    public void testMoveWhenMoveResultsInCheckmate() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isCheckmated(Colour.BLACK)).thenReturn(true);
        Game game = new Game(GameStatus.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(mockPiecesState, state.getState());
        assertEquals(GameStatus.OVER_CHECKMATE, state.getStatus());
    }

    @Test
    public void testMoveWhenMoveResultsInStalemate() {
        // Given
        when(board.isChecked(Colour.WHITE)).thenReturn(false);
        when(board.isStalemated(Colour.BLACK)).thenReturn(true);
        Game game = new Game(GameStatus.IN_PROGRESS, board, Colour.WHITE);

        // When
        GameState state = game.move(move);

        // Then
        assertEquals(mockPiecesState, state.getState());
        assertEquals(GameStatus.OVER_STALEMATE, state.getStatus());
    }
}
