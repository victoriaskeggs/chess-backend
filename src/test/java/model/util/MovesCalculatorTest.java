package model.util;

import model.Colour;
import model.PieceType;
import model.Square;
import model.piece.PieceState;
import model.pieces.PiecesState;
import org.junit.jupiter.api.BeforeEach;
import testutil.CollectionUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MovesCalculatorTest {

    /**
     * Object under test
     */
    private MovesCalculator movesCalculator;

    @BeforeEach
    public void setup() {
        movesCalculator = new MovesCalculator();
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|B |B_|W |__|  |
     * 4: |  |__|  |__|WP|__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a pawn (WP):
     * - can take an enemy piece on a diagonal
     * - cannot take a non-enemy piece on a diagonal
     * - cannot move forwards (as a white piece) if the square is blocked by a piece
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForPawnWhenPawnIsWhite() {
        // Given
        PieceState pawnState = new PieceState(PieceType.PAWN, Colour.WHITE, Square.E4);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                        new PieceState(PieceType.PAWN, Colour.BLACK, Square.D5),
                        new PieceState(PieceType.PAWN, Colour.BLACK, Square.E5),
                        new PieceState(PieceType.PAWN, Colour.WHITE, Square.F5),
                        pawnState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForPawn(pawnState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(new Square[] {Square.D5});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(new Square[] {Square.D5, Square.F5});
        assertEquals(expectedThreatened, actualThreatened);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|BP|__|  |__|
     * 5: |__|  |__|WK|__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a pawn (BP):
     * - cannot move on the diagonal if the square is empty
     * - cannot take the enemy king on a diagonal
     * - can move forwards (as a black piece) if the square is empty
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForPawnWhenPawnIsBlack() {
        // Given
        PieceState pawnState = new PieceState(PieceType.PAWN, Colour.BLACK, Square.E6);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.KING, Colour.WHITE, Square.D5), pawnState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForPawn(pawnState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(new Square[] {Square.E5});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(new Square[] {Square.D5, Square.F5});
        assertEquals(expectedThreatened, actualThreatened);
    }

    /**
     * 8: |  |__|  |__|WP|__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a pawn (WP) on the top edge of a board cannot move
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForPawnWhenPawnIsOnEdge() {
        // Given
        PieceState pawnState = new PieceState(PieceType.PAWN, Colour.WHITE, Square.E8);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {pawnState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForPawn(pawnState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        assertEquals(new HashSet<>(), actualMoveable);
        assertEquals(new HashSet<>(), actualThreatened);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|W |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|B |__|WC|__|BK|__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a castle (WC):
     * - can move vertically and horizontally
     * - cannot move off the edge of the board
     * - cannot take the enemy king or move past it
     * - cannot take non-enemy pieces or move past them
     * - can take enemy pieces but cannot move past them
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForCastle() {
        // Given
        PieceState castleState = new PieceState(PieceType.CASTLE, Colour.WHITE, Square.D5);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.D7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B5),
                new PieceState(PieceType.KING, Colour.BLACK, Square.F5),
                castleState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForCastle(castleState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(
                new Square[] {Square.D6, Square.B5, Square.C5, Square.E5, Square.D4, Square.D3, Square.D2, Square.D1});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(
                new Square[] {Square.D6, Square.B5, Square.C5, Square.E5, Square.D4, Square.D3, Square.D2, Square.D1,
                Square.B5, Square.F5, Square.D7});
        assertEquals(expectedThreatened, actualThreatened);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|WK|__|  |__|  |
     * 6: |  |BH|  |__|  |__|  |__|
     * 5: |B_|B |__|B |__|  |__|  |
     * 4: |W |B_|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a knight (BH):
     * - can move in an L-shape
     * - cannot move off the edge of the board
     * - cannot take the enemy king
     * - cannot take non-enemy pieces
     * - can take enemy pieces
     * - is not blocked by pieces in between it and squares an L-shape away
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForKnight() {
        // Given
        PieceState knightState = new PieceState(PieceType.KNIGHT, Colour.BLACK, Square.B6);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.KING, Colour.WHITE, Square.D7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.A5),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B5),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.D5),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.A4),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B4),
                knightState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForKnight(knightState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(new Square[] {Square.A4, Square.C4, Square.A8, Square.C8});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(
                new Square[] {Square.A4, Square.C4, Square.A8, Square.C8, Square.D5, Square.D7});
        assertEquals(expectedThreatened, actualThreatened);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|B |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|WB|__|  |__|  |
     * 4: |  |__|W |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|BK|__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a bishop (WB):
     * - can move diagonally
     * - cannot move off the edge of the board
     * - cannot take the enemy king or move past it
     * - cannot take non-enemy pieces or move past them
     * - can take enemy pieces but cannot move past them
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForBishop() {
        // Given
        PieceState bishopState = new PieceState(PieceType.BISHOP, Colour.WHITE, Square.D5);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.KING, Colour.BLACK, Square.G2),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B7),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.C4),
                bishopState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForBishop(bishopState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(
                new Square[] {Square.B7, Square.C6, Square.E6, Square.F7, Square.G8, Square.E4, Square.F3});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(
                new Square[] {Square.B7, Square.C6, Square.E6, Square.F7, Square.G8, Square.E4, Square.F3,
                        Square.B7, Square.G2, Square.C4});
        assertEquals(expectedThreatened, actualThreatened);
    }

    /**
     * 8: |  |__|  |W_|  |__|  |__|
     * 7: |__|B |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|BQ|__|B |__|  |
     * 4: |  |__|W |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|WK|__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a queen (BQ):
     * - can move diagonally and horizontally
     * - cannot move off the edge of the board
     * - cannot take the enemy king or move past it
     * - cannot take non-enemy pieces or move past them
     * - can take enemy pieces but cannot move past them
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForQueen() {
        // Given
        PieceState queenState = new PieceState(PieceType.QUEEN, Colour.BLACK, Square.D5);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.KING, Colour.WHITE, Square.G2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.D8),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.F5),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.C4),
                queenState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForQueen(queenState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(
                new Square[] {Square.D8, Square.D7, Square.D6, Square.C6, Square.E6, Square.F7, Square.G8,
                Square.A5, Square.B5, Square.C5, Square.E5, Square.C4, Square.D4, Square.D3, Square.D2, Square.D1,
                Square.E4, Square.F3});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(
                new Square[] {Square.D8, Square.D7, Square.D6, Square.C6, Square.E6, Square.F7, Square.G8,
                        Square.A5, Square.B5, Square.C5, Square.E5, Square.C4, Square.D4, Square.D3, Square.D2, Square.D1,
                        Square.E4, Square.F3, Square.F5, Square.B7, Square.G2});
        assertEquals(expectedThreatened, actualThreatened);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|W |WK|  |__|  |__|
     * 3: |__|  |B_|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     *
     * Tests that a king (WK):
     * - can move to adjacent squares
     * - cannot take non-enemy pieces
     * - can take enemy pieces
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testCalculateThreatenedAndMoveableSquaresForKing() {
        // Given
        PieceState kingState = new PieceState(PieceType.KING, Colour.WHITE, Square.D4);
        Set<PieceState> pieceStates = CollectionUtil.createSet(new PieceState[] {
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.C4),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.C3),
                kingState});

        // When
        movesCalculator.calculateMoveableAndThreatenedSquaresForKing(kingState, new PiecesState(pieceStates));
        Set<Square> actualMoveable = movesCalculator.getMoveableSquares();
        Set<Square> actualThreatened = movesCalculator.getThreatenedSquares();

        // Then
        Set<Square> expectedMoveable = CollectionUtil.createSet(
                new Square[] {Square.C5, Square.D5, Square.E5, Square.E4, Square.C3, Square.D3, Square.E3});
        assertEquals(expectedMoveable, actualMoveable);

        Set<Square> expectedThreatened = CollectionUtil.createSet(
                new Square[] {Square.C5, Square.D5, Square.E5, Square.E4, Square.C3, Square.D3, Square.E3, Square.C4});
        assertEquals(expectedThreatened, actualThreatened);
    }
}
