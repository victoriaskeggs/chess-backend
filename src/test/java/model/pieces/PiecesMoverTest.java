package model.pieces;

import model.Colour;
import model.Move;
import model.PieceType;
import model.Square;
import model.exception.ChessException;
import model.piece.Piece;
import model.piece.PieceFactory;
import model.piece.PieceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import testutil.CollectionUtil;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PiecesMoverTest {

    private PieceState mockPawn1State;

    private PieceState mockPawn2State;

    private PieceState mockKingState;

    private Piece mockPawn1;

    private Piece mockPawn2;

    private Piece mockKing;

    /**
     * Object under test
     */
    @InjectMocks
    private PiecesMover piecesMover;

    @Mock(name = "pieceFactory")
    private PieceFactory pieceFactory;

    @BeforeEach
    public void setupFakePieceMover() {
        // Inject the PieceFactory mock
        piecesMover = new PiecesMover();
        MockitoAnnotations.initMocks(this);

        // Set up first mock pawn to return a state (white pawn at B5)
        mockPawn1State = new PieceState(PieceType.PAWN, Colour.WHITE, Square.B5);
        mockPawn1 = connectStateAndPiece(mockPawn1State);

        // Set up second mock pawn to return a state (black pawn at A3)
        mockPawn2State = new PieceState(PieceType.PAWN, Colour.BLACK, Square.A3);
        mockPawn2 = connectStateAndPiece(mockPawn2State);

        // Set up mock king to return a state (white king at D1)
        mockKingState = new PieceState(PieceType.KING, Colour.WHITE, Square.D1);
        mockKing = connectStateAndPiece(mockKingState);

        // Add mock pieces to piece mover under test
        PiecesState mockPiecesState = new PiecesState(
                CollectionUtil.createSet(new PieceState[] {mockPawn1State, mockPawn2State, mockKingState}));
        piecesMover.addPieces(mockPiecesState);
    }

    @Test
    public void testAddPieces() {
        // Given
        PieceState mockPawn3State = new PieceState(PieceType.PAWN, Colour.BLACK, Square.A7);
        connectStateAndPiece(mockPawn3State);

        PieceState mockQueenState = new PieceState(PieceType.QUEEN, Colour.WHITE, Square.A4);
        connectStateAndPiece(mockQueenState);

        PiecesState piecesState = mock(PiecesState.class);
        when(piecesState.getPieceStates()).thenReturn(
                CollectionUtil.createSet(new PieceState[] {mockPawn3State, mockQueenState}));

        // When
        piecesMover.addPieces(piecesState);

        // Then
        Set<PieceState> expected = CollectionUtil.createSet(
                new PieceState[] {mockPawn1State, mockPawn2State, mockPawn3State, mockQueenState, mockKingState});
        assertEquals(expected, piecesMover.generatePiecesState().getPieceStates());
    }

    @Test
    public void testClearPieces() {
        // When
        piecesMover.clearPieces();

        // Then
        assertEquals(new HashSet<>(), piecesMover.generatePiecesState().getPieceStates());
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
        when(mockPawn1.getState()).thenAnswer((Answer<PieceState>) invocation -> new PieceState(PieceType.PAWN, Colour.WHITE, mockPawn1Square[0]));

        // When
        piecesMover.move(new Move(mockPawn1State, Square.B6));

        // Then
        verify(mockPawn1).moveTo(Square.B6);

        Set<PieceState> expectedPieceStates = getInitialPieceStates();
        expectedPieceStates.remove(mockPawn1State);
        expectedPieceStates.add(new PieceState(PieceType.PAWN, Colour.WHITE, Square.B6));
        verifyUpdate(expectedPieceStates);
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
        when(mockPawn1.getState()).thenAnswer((Answer<PieceState>) invocation -> new PieceState(PieceType.PAWN, Colour.WHITE, mockPawn1Square[0]));

        piecesMover.move(new Move(mockPawn1State, Square.B6));

        // When
        piecesMover.undoMove();

        // Then
        verify(mockPawn1).moveTo(Square.B6);
        verify(mockPawn1).moveToUnchecked(Square.B5);
        verifyUpdate(getInitialPieceStates());
    }

    @Test
    public void testGeneratePiecesState() {
        // When
        Set<PieceState> actual = piecesMover.generatePiecesState().getPieceStates();

        // Then
        Set<PieceState> expected = CollectionUtil.createSet(
                new PieceState[] {mockPawn1State, mockPawn2State, mockKingState});
        assertEquals(expected, actual);
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

    /**
     * Creates a mock piece for the given state, sets up mock piece to return the state and sets up
     * mock piece factory to return the piece given the state.
     * @param state
     * @return mocked piece
     */
    private Piece connectStateAndPiece(PieceState state) {
        Piece mockPiece = mock(Piece.class);
        when(mockPiece.getState()).thenReturn(state);
        when(pieceFactory.createPiece(state)).thenReturn(mockPiece);
        return mockPiece;
    }

    /**
     * Verifies that the mock pieces received an update() call with the expected colour locations and king locations.
     * @param expectedPieceStates
     */
    private void verifyUpdate(Set<PieceState> expectedPieceStates) {
        for (Piece mock : new Piece[] {mockPawn1, mockPawn2, mockKing}) {
            ArgumentCaptor<PiecesState> piecesStateCaptor = ArgumentCaptor.forClass(PiecesState.class);
            verify(mock, atLeast(1)).update(piecesStateCaptor.capture());
            assertEquals(expectedPieceStates, piecesStateCaptor.getValue().getPieceStates());
        }
    }

    private Set<PieceState> getInitialPieceStates() {
        return CollectionUtil.createSet(new PieceState[] {mockPawn1State, mockPawn2State, mockKingState});
    }
}
