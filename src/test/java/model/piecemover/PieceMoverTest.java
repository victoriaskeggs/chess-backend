package model.piecemover;

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
import testutil.CollectionUtil;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PieceMoverTest {
    @Mock
    private Piece mockPawn1;

    @Mock
    private Piece mockPawn2;

    @Mock
    private Piece mockKing;

    private PieceMover fakePieceMover;

    @BeforeEach
    public void setupFakePieceMover() {
        // Set up mocks to remove dependency on Piece
        MockitoAnnotations.initMocks(this);
        setupPiece(mockPawn1, Colour.WHITE, Square.B5);
        setupPiece(mockPawn2, Colour.BLACK, Square.A3);
        setupPiece(mockKing, Colour.WHITE, Square.D1);

        // Set up fake PieceMover
        fakePieceMover = new PieceMover();
        fakePieceMover.piecesByType.put(PieceType.PAWN, CollectionUtil.createSet(new Piece[] {mockPawn1, mockPawn2}));
        fakePieceMover.piecesByType.put(PieceType.KING, CollectionUtil.createSet(new Piece[] {mockKing}));
    }

    @Test
    public void testSetupPiecesAsUpdateListeners() {
        // When
        fakePieceMover.setupPiecesAsPiecesStateListeners();

        // Then
        assertEquals(fakePieceMover.piecesStateListeners, CollectionUtil.createSet(new Piece [] {mockPawn1, mockPawn2, mockKing}));
        verifyUpdate(getInitialColourLocations(), getInitialKingLocations());
    }

    @Test
    public void testMove() {
        // Given
        when(mockPawn1.canMoveTo(Square.B6)).thenReturn(true);

        // When
        fakePieceMover.move(new Move(PieceType.PAWN, Colour.WHITE, Square.B5, Square.B6));

        // Then
        verify(mockPawn1).moveTo(Square.B6);
    }

    @Test
    public void testFirePiecesUpdateEvent() {
        // Given
        when(mockPawn1.getCurrentSquare()).thenReturn(Square.B6);
        fakePieceMover.piecesStateListeners = CollectionUtil.createSet(new Piece[] {mockPawn1, mockPawn2, mockKing});

        // When
        fakePieceMover.firePiecesStateEvent();

        // Then
        Map<Square, Colour> colourLocations = getInitialColourLocations();
        colourLocations.remove(Square.B5);
        colourLocations.put(Square.B6, Colour.WHITE);
        verifyUpdate(colourLocations, getInitialKingLocations());
    }

    @Test
    public void testFindPieceWhenPieceDoesNotExist() {
        // When
        try {
            fakePieceMover.findPiece(PieceType.PAWN, Colour.WHITE, Square.H2);
        } catch (ChessException exception) {

            // Then
            assertEquals("There is no WHITE PAWN at square H2", exception.getMessage());
        }
    }

    @Test
    public void testFindPieceWhenPieceExists() {
        // When
        Piece actual = fakePieceMover.findPiece(PieceType.KING, Colour.WHITE, Square.D1);

        // Then
        assertEquals(mockKing, actual);
    }

    @Test
    public void testFindPieces() {
        // When
        Set<Piece> actual = fakePieceMover.findPieces(PieceType.PAWN, Colour.BLACK);

        // Then
        Set<Piece> expected = CollectionUtil.createSet(new Piece[] {mockPawn2});
        assertEquals(expected, actual);
    }

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
            verify(mock).update(piecesStateCaptor.capture());
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
