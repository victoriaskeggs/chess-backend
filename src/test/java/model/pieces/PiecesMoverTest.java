package model.pieces;

import model.Colour;
import model.Move;
import model.PieceType;
import model.Square;
import model.exception.ChessException;
import model.piece.Piece;
import model.piecestate.PiecesState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import testutil.CollectionUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PiecesMoverTest {
    @Mock
    private Piece mockPawn1;

    @Mock
    private Piece mockPawn2;

    @Mock
    private Piece mockKing;

    /**
     * Object under test
     */
    private PiecesMover piecesMover;

    @BeforeEach
    public void setupFakePieceMover() {
        // Set up mocks to remove dependency on Piece
        MockitoAnnotations.initMocks(this);
        setupPiece(mockPawn1, Colour.WHITE, Square.B5);
        setupPiece(mockPawn2, Colour.BLACK, Square.A3);
        setupPiece(mockKing, Colour.WHITE, Square.D1);

        // Set up fake PieceMover
        Map<PieceType, Set<Piece>> piecesByType = new HashMap<>();
        piecesByType.put(PieceType.PAWN, CollectionUtil.createSet(new Piece[] {mockPawn1, mockPawn2}));
        piecesByType.put(PieceType.KING, CollectionUtil.createSet(new Piece[] {mockKing}));
        piecesMover = new PiecesMover(piecesByType);
    }

    @Test
    public void testAddPieces() {
        // Given
        Map<PieceType, Set<Piece>> piecesByType = new HashMap<>();

        Piece mockPawn3 = mock(Piece.class);
        setupPiece(mockPawn3, Colour.BLACK, Square.A7);
        piecesByType.put(PieceType.PAWN, CollectionUtil.createSet(new Piece[] {mockPawn3}));

        Piece mockQueen = mock(Piece.class);
        setupPiece(mockQueen, Colour.WHITE, Square.A4);
        piecesByType.put(PieceType.QUEEN, CollectionUtil.createSet(new Piece[] {mockQueen}));

        // When
        piecesMover.addPieces(piecesByType);

        // Then
        Set<Piece> expected = CollectionUtil.createSet(new Piece[] {mockPawn1, mockPawn2, mockPawn3, mockQueen, mockKing});
        assertEquals(expected, piecesMover.getPieces());
    }

    @Test
    public void testClearPieces() {
        // When
        piecesMover.clearPieces();

        // Then
        assertEquals(new HashSet<>(), piecesMover.getPieces());
    }

    @Test
    public void testMove() {
        // Given
        when(mockPawn1.canMoveTo(Square.B6)).thenReturn(true);

        final Square[] mockPawn1Square = {Square.B5};
        doAnswer(invocation -> {
            mockPawn1Square[0] = Square.B6; // update square mock pawn is on
            return null;
        }).when(mockPawn1).moveTo(Square.B6);
        when(mockPawn1.getCurrentSquare()).thenAnswer((Answer<Square>) invocation -> mockPawn1Square[0]);

        // When
        piecesMover.move(new Move(PieceType.PAWN, Colour.WHITE, Square.B5, Square.B6));

        // Then
        verify(mockPawn1).moveTo(Square.B6);

        Map<Square, Colour> colourLocations = getInitialColourLocations();
        colourLocations.remove(Square.B5);
        colourLocations.put(Square.B6, Colour.WHITE);
        verifyUpdate(colourLocations, getInitialKingLocations());
    }

    @Test
    public void testUndoMove() {
        // Given
        when(mockPawn1.canMoveTo(Square.B6)).thenReturn(true);

        final Square[] mockPawn1Square = {Square.B5};
        doAnswer(invocation -> {
            mockPawn1Square[0] = Square.B6; // update square mock pawn is on
            return null;
        }).when(mockPawn1).moveTo(Square.B6);
        doAnswer(invocation -> {
            mockPawn1Square[0] = Square.B5; // update square mock pawn is on
            return null;
        }).when(mockPawn1).moveToUnchecked(Square.B5);
        when(mockPawn1.getCurrentSquare()).thenAnswer((Answer<Square>) invocation -> mockPawn1Square[0]);

        piecesMover.move(new Move(PieceType.PAWN, Colour.WHITE, Square.B5, Square.B6));

        // When
        piecesMover.undoMove();

        // Then
        verify(mockPawn1).moveTo(Square.B6);
        verify(mockPawn1).moveToUnchecked(Square.B5);
        verifyUpdate(getInitialColourLocations(), getInitialKingLocations());
    }

    @Test
    public void testFindPieceWhenPieceDoesNotExist() {
        // When
        try {
            piecesMover.findPiece(PieceType.PAWN, Colour.WHITE, Square.H2);
        } catch (ChessException exception) {

            // Then
            assertEquals("There is no WHITE PAWN at square H2", exception.getMessage());
        }
    }

    @Test
    public void testFindPieceWhenPieceExists() {
        // When
        Piece actual = piecesMover.findPiece(PieceType.KING, Colour.WHITE, Square.D1);

        // Then
        assertEquals(mockKing, actual);
    }

    @Test
    public void testFindPieces() {
        // When
        Set<Piece> actual = piecesMover.findPieces(PieceType.PAWN, Colour.BLACK);

        // Then
        Set<Piece> expected = CollectionUtil.createSet(new Piece[] {mockPawn2});
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPieces() {
        // When
        Set<Piece> actual = piecesMover.getPieces();

        // Then
        Set<Piece> expected = CollectionUtil.createSet(new Piece[] {mockPawn1, mockPawn2, mockKing});
        assertEquals(expected, actual);
    }

    // TODO tests for addPieces and tests for clearPieces()

    /**
     * Sets up a mock piece to return the provided colour and square when queried. Sets piece
     * to return true when queried isAlive().
     * @param piece
     * @param colour returned when queried getColour()
     * @param square returned when queried getCurrentSquare()
     */
    private void setupPiece(Piece piece, Colour colour, Square square) {
        when(piece.isAlive()).thenReturn(true);
        when(piece.getCurrentSquare()).thenReturn(square);
        when(piece.getColour()).thenReturn(colour);
    }

    /**
     * Verifies that the mock pieces received an update() call with the expected colour locations and king locations.
     * @param expectedColourLocations
     * @param expectedKingLocations
     */
    private void verifyUpdate(Map<Square, Colour> expectedColourLocations, Set<Square> expectedKingLocations) {
        for (Piece mock : new Piece[] {mockPawn1, mockPawn2, mockKing}) {
            ArgumentCaptor<PiecesState> piecesStateCaptor = ArgumentCaptor.forClass(PiecesState.class);
            verify(mock, atLeast(1)).update(piecesStateCaptor.capture());
            assertEquals(expectedKingLocations, piecesStateCaptor.getValue().getKingLocations());
            assertEquals(expectedColourLocations, piecesStateCaptor.getValue().getColourLocations());
        }
    }

    private Map<Square, Colour> getInitialColourLocations() {
        return CollectionUtil.createMap(
                new Square[] {Square.B5, Square.A3, Square.D1},
                new Colour[] {Colour.WHITE, Colour.BLACK, Colour.WHITE});
    }

    private Set<Square> getInitialKingLocations() {
        return CollectionUtil.createSet(new Square[] {Square.D1});
    }
}
