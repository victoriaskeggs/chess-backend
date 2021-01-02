package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;
import model.pieces.PiecesState;
import org.junit.jupiter.api.Test;
import testutil.CollectionUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

public class PieceFactoryTest {

    @Test
    public void testCreateDefaultPieces() {
        // When
        PiecesState actual = new PieceFactory().createDefaultPiecesState();

        // Then
        Set<PieceState> expectedPieceStates = new HashSet<>();
        expectedPieceStates.addAll(Arrays.asList(
                // Expected black pieces
                new PieceState(PieceType.KING, Colour.BLACK, Square.E8),
                new PieceState(PieceType.QUEEN, Colour.BLACK, Square.D8),
                new PieceState(PieceType.BISHOP, Colour.BLACK, Square.C8),
                new PieceState(PieceType.BISHOP, Colour.BLACK, Square.F8),
                new PieceState(PieceType.KNIGHT, Colour.BLACK, Square.B8),
                new PieceState(PieceType.KNIGHT, Colour.BLACK, Square.G8),
                new PieceState(PieceType.CASTLE, Colour.BLACK, Square.A8),
                new PieceState(PieceType.CASTLE, Colour.BLACK, Square.H8),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.A7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.C7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.D7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.E7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.F7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.G7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.H7),

                // Expected white pieces
                new PieceState(PieceType.KING, Colour.WHITE, Square.E1),
                new PieceState(PieceType.QUEEN, Colour.WHITE, Square.D1),
                new PieceState(PieceType.BISHOP, Colour.WHITE, Square.C1),
                new PieceState(PieceType.BISHOP, Colour.WHITE, Square.F1),
                new PieceState(PieceType.KNIGHT, Colour.WHITE, Square.B1),
                new PieceState(PieceType.KNIGHT, Colour.WHITE, Square.G1),
                new PieceState(PieceType.CASTLE, Colour.WHITE, Square.A1),
                new PieceState(PieceType.CASTLE, Colour.WHITE, Square.H1),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.A2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.B2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.C2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.D2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.E2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.F2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.G2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.H2)));

        assertEquals(expectedPieceStates, actual.getPieceStates());
    }

    @Test
    public void testCreatePieces() {
        // Given
        Set<PieceState> pieces = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.KING, Colour.BLACK, Square.E2),
                new PieceState(PieceType.BISHOP, Colour.WHITE, Square.A7)
        });

        // When
        Set<Piece> actual = new PieceFactory().createPieces(new PiecesState(pieces));

        // Then
        Set<Piece> expectedPieces = CollectionUtil.createSet(new Piece[] {
                new King(Colour.BLACK, Square.E2), new Bishop(Colour.WHITE, Square.A7)});

        assertEquals(expectedPieces, actual);
    }

    @Test
    public void testCreatePieceWhenPieceIsPawn() {
        // Given
        PieceState expected = new PieceState(PieceType.PAWN, Colour.WHITE, Square.H2);

        // When
        Piece piece = new PieceFactory().createPiece(expected);

        // Then
        assertEquals(Pawn.class, piece.getClass());
        assertEquals(expected, piece.getState());
    }

    @Test
    public void testCreatePieceWhenPieceIsCastle() {
        // Given
        PieceState expected = new PieceState(PieceType.CASTLE, Colour.BLACK, Square.H2);

        // When
        Piece piece = new PieceFactory().createPiece(expected);

        // Then
        assertEquals(Castle.class, piece.getClass());
        assertEquals(expected, piece.getState());
    }

    @Test
    public void testCreatePieceWhenPieceIsKnight() {
        // Given
        PieceState expected = new PieceState(PieceType.KNIGHT, Colour.WHITE, Square.H2);

        // When
        Piece piece = new PieceFactory().createPiece(expected);

        // Then
        assertEquals(Knight.class, piece.getClass());
        assertEquals(expected, piece.getState());
    }

    @Test
    public void testCreatePieceWhenPieceIsBishop() {
        // Given
        PieceState expected = new PieceState(PieceType.BISHOP, Colour.BLACK, Square.H2);

        // When
        Piece piece = new PieceFactory().createPiece(expected);

        // Then
        assertEquals(Bishop.class, piece.getClass());
        assertEquals(expected, piece.getState());
    }

    @Test
    public void testCreatePieceWhenPieceIsQueen() {
        // Given
        PieceState expected = new PieceState(PieceType.QUEEN, Colour.WHITE, Square.H2);

        // When
        Piece piece = new PieceFactory().createPiece(expected);

        // Then
        assertEquals(Queen.class, piece.getClass());
        assertEquals(expected, piece.getState());
    }

    @Test
    public void testCreatePieceWhenPieceIsKing() {
        // Given
        PieceState expected = new PieceState(PieceType.KING, Colour.BLACK, Square.H2);

        // When
        Piece piece = new PieceFactory().createPiece(expected);

        // Then
        assertEquals(King.class, piece.getClass());
        assertEquals(expected, piece.getState());
    }
}
