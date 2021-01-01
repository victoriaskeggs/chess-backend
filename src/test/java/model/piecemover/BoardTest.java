package model.piecemover;

import model.Colour;
import model.Move;
import model.PieceType;
import model.exception.ChessException;
import model.piece.Piece;
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

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMove() {
        // Given
        Move move = mock(Move.class);

        // When
        board.move(move);

        // Then
        verify(piecesMover).move(move);
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
        Piece mockKing = mock(Piece.class);
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE))
                .thenReturn(CollectionUtil.createSet(new Piece[] {mockKing}));

        Set<Piece> mockPieces = CollectionUtil.createSet(new Piece[] {mock(Piece.class)});
        when(piecesMover.getPieces()).thenReturn(mockPieces);

        when(endgameHelper.isInCheck(mockKing, mockPieces)).thenReturn(true);

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
        Piece mockKing = mock(Piece.class);
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE))
                .thenReturn(CollectionUtil.createSet(new Piece[] {mockKing}));

        Set<Piece> mockPieces = CollectionUtil.createSet(new Piece[] {mock(Piece.class)});
        when(piecesMover.getPieces()).thenReturn(mockPieces);

        when(endgameHelper.isInCheckmate(mockKing, mockPieces)).thenReturn(true);

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
        Piece mockKing = mock(Piece.class);
        when(piecesMover.findPieces(PieceType.KING, Colour.WHITE))
                .thenReturn(CollectionUtil.createSet(new Piece[] {mockKing}));

        Set<Piece> mockPieces = CollectionUtil.createSet(new Piece[] {mock(Piece.class)});
        when(piecesMover.getPieces()).thenReturn(mockPieces);

        when(endgameHelper.isInStalemate(mockKing, mockPieces)).thenReturn(true);

        // When
        boolean isStalemated = board.isStalemated(Colour.WHITE);

        // Then
        assertTrue(isStalemated);
    }
}
