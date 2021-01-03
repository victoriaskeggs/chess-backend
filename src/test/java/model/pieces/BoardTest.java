package model.pieces;

import model.Colour;
import model.Move;
import model.PieceType;
import model.exception.ChessException;
import model.piece.Piece;
import model.piece.PieceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testutil.CollectionUtil;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardTest {

    @Mock(name = "pieceMover")
    private PiecesMover piecesMover;

    @Mock(name = "endgameHelper")
    private EndgameHelper endgameHelper;

    /**
     * Object under test
     */
    @InjectMocks
    private Board board;

    @Mock
    private Piece mockKing;

    @Mock
    private PieceState mockKingState;

    @Mock
    private PiecesState mockPiecesState;

    @BeforeEach
    public void setupMocks() {
        // Inject dependencies
        MockitoAnnotations.initMocks(this);

        // Mock king for end-of-game checking
        mockKingState = mock(PieceState.class);
        when(mockKing.getState()).thenReturn(mockKingState);

        // Mock state of pieces on board
        mockPiecesState = mock(PiecesState.class);
        when(piecesMover.generatePiecesState()).thenReturn(mockPiecesState);
    }

    @Test
    public void testMove() {
        // Given
        Move move = mock(Move.class);
        PiecesState piecesState = mock(PiecesState.class);
        when(piecesMover.move(move)).thenReturn(piecesState);

        // When
        PiecesState actualPiecesState = board.move(move);

        // Then
        verify(piecesMover).move(move);
        assertEquals(piecesState, actualPiecesState);
    }

    @Test
    public void testIsCheckedWhenThereIsNoKing() {
        // Given
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE)).thenReturn(new HashSet<>());

        // When
        try {
            board.isChecked(Colour.WHITE);
            fail();
        } catch (ChessException exception) {

            // Then
            assertEquals("There is not exactly one WHITE KING on the board.", exception.getMessage());
            verify(piecesMover).findPieces(PieceType.KING, Colour.WHITE);
            verify(piecesMover, never()).findPieces(PieceType.KING, Colour.BLACK);
        }
    }

    @Test
    public void testIsCheckedWhenThereIsAKing() {
        // Given
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE))
                .thenReturn(CollectionUtil.createSet(new Piece[] {mockKing}));
        when(endgameHelper.isInCheck(mockKingState, mockPiecesState)).thenReturn(true);

        // When
        boolean isChecked = board.isChecked(Colour.WHITE);

        // Then
        assertTrue(isChecked);
    }

    @Test
    public void testIsCheckmatedWhenThereIsNoKing() {
        // Given
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE)).thenReturn(new HashSet<>());

        // When
        try {
            board.isCheckmated(Colour.WHITE);
            fail();
        } catch (ChessException exception) {

            // Then
            assertEquals("There is not exactly one WHITE KING on the board.", exception.getMessage());
            verify(piecesMover).findPieces(PieceType.KING, Colour.WHITE);
            verify(piecesMover, never()).findPieces(PieceType.KING, Colour.BLACK);
        }
    }

    @Test
    public void testIsCheckmatedWhenThereIsAKing() {
        // Given
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE))
                .thenReturn(CollectionUtil.createSet(new Piece[] {mockKing}));
        when(endgameHelper.isInCheckmate(mockKingState, mockPiecesState)).thenReturn(true);

        // When
        boolean isCheckmated = board.isCheckmated(Colour.WHITE);

        // Then
        assertTrue(isCheckmated);
    }

    @Test
    public void testIsStalematedWhenThereIsNoKing() {
        // Given
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE)).thenReturn(new HashSet<>());

        // When
        try {
            board.isStalemated(Colour.WHITE);
            fail();
        } catch (ChessException exception) {

            // Then
            assertEquals("There is not exactly one WHITE KING on the board.", exception.getMessage());
            verify(piecesMover).findPieces(PieceType.KING, Colour.WHITE);
            verify(piecesMover, never()).findPieces(PieceType.KING, Colour.BLACK);
        }
    }

    @Test
    public void testIsStalematedWhenThereIsAKing() {
        // Given
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE))
                .thenReturn(CollectionUtil.createSet(new Piece[] {mockKing}));
        when(endgameHelper.isInStalemate(mockKingState, mockPiecesState)).thenReturn(true);

        // When
        boolean isStalemated = board.isStalemated(Colour.WHITE);

        // Then
        assertTrue(isStalemated);
    }
}
