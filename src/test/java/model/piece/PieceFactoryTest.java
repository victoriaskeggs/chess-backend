package model.piece;

import model.Colour;
import model.PieceType;
import model.Square;
import org.junit.jupiter.api.Test;
import testutil.CollectionUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

public class PieceFactoryTest {

    @Test
    public void testCreateDefaultPieces() {
        // When
        Map<PieceType, Set<Piece>> actual = new PieceFactory().createDefaultPieces();

        // Then
        Map<PieceType, Set<Piece>> expected = new HashMap<>();
        Set<Piece> kings = CollectionUtil.createSet(
                new King[] {new King(Colour.BLACK, Square.E8), new King(Colour.WHITE, Square.E1)});
        expected.put(PieceType.KING, kings);

        Set<Piece> queens = CollectionUtil.createSet(
                new Queen[] {new Queen(Colour.BLACK, Square.D8), new Queen(Colour.WHITE, Square.D1)});
        expected.put(PieceType.QUEEN, queens);

        Set<Piece> bishops = CollectionUtil.createSet(
                new Bishop[] {new Bishop(Colour.BLACK, Square.C8), new Bishop(Colour.BLACK, Square.F8),
                        new Bishop(Colour.WHITE, Square.C1), new Bishop(Colour.WHITE, Square.F1)});
        expected.put(PieceType.BISHOP, bishops);

        Set<Piece> knights = CollectionUtil.createSet(
                new Knight[] {new Knight(Colour.BLACK, Square.B8), new Knight(Colour.BLACK, Square.G8),
                        new Knight(Colour.WHITE, Square.B1), new Knight(Colour.WHITE, Square.G1)});
        expected.put(PieceType.KNIGHT, knights);

        Set<Piece> castles = CollectionUtil.createSet(
                new Castle[] {new Castle(Colour.BLACK, Square.A8), new Castle(Colour.BLACK, Square.H8),
                        new Castle(Colour.WHITE, Square.A1), new Castle(Colour.WHITE, Square.H1)});
        expected.put(PieceType.CASTLE, castles);
        
        Set<Piece> pawns = CollectionUtil.createSet(
                new Pawn[] {new Pawn(Colour.BLACK, Square.A7), new Pawn(Colour.BLACK, Square.B7),
                        new Pawn(Colour.BLACK, Square.C7), new Pawn(Colour.BLACK, Square.D7),
                        new Pawn(Colour.BLACK, Square.E7), new Pawn(Colour.BLACK, Square.F7),
                        new Pawn(Colour.BLACK, Square.G7), new Pawn(Colour.BLACK, Square.H7),
                        new Pawn(Colour.WHITE, Square.A2), new Pawn(Colour.WHITE, Square.B2),
                        new Pawn(Colour.WHITE, Square.C2), new Pawn(Colour.WHITE, Square.D2),
                        new Pawn(Colour.WHITE, Square.E2), new Pawn(Colour.WHITE, Square.F2),
                        new Pawn(Colour.WHITE, Square.G2), new Pawn(Colour.WHITE, Square.H2)});
        expected.put(PieceType.PAWN, pawns);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateCopyOfPieces() {
        // Given
        Set<Piece> pieces = CollectionUtil.createSet(new Piece[] {
                new King(Colour.BLACK, Square.E2),
                new Bishop(Colour.WHITE, Square.A7)
        });

        // When
        Map<PieceType, Set<Piece>> actual = new PieceFactory().createCopyOfPieces(pieces);

        // Then
        Set<Piece> kings = CollectionUtil.createSet(new King[] {new King(Colour.BLACK, Square.E2)});
        Set<Piece> bishops = CollectionUtil.createSet(new Bishop[] {new Bishop(Colour.WHITE, Square.A7)});

        Map<PieceType, Set<Piece>> expected = new HashMap<>();
        expected.put(PieceType.KING, kings);
        expected.put(PieceType.BISHOP, bishops);

        assertEquals(expected, actual);
    }
}
