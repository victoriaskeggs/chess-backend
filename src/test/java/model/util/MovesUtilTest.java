package model.util;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import testutil.CollectionUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MovesUtilTest {

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
    public void testGetMoveableSquaresForPawnWhenPawnIsWhite() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.D5, Square.E5, Square.F5, Square.E4},
                new Colour[] {Colour.BLACK, Colour.BLACK, Colour.WHITE, Colour.WHITE});
        PiecesState state = new PiecesState(colourLocations, new HashSet<>());

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForPawn(Square.E4, Colour.WHITE, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(new Square[] {Square.D5});
        assertEquals(expected, actual);
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
    public void testGetMoveableSquaresForPawnWhenPawnIsBlack() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.D5, Square.E6},
                new Colour[] {Colour.WHITE, Colour.BLACK});
        Set<Square> kingLocations = CollectionUtil.createSet(new Square[] {Square.D5});
        PiecesState state = new PiecesState(colourLocations, kingLocations);

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForPawn(Square.E6, Colour.BLACK, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(new Square[] {Square.E5});
        assertEquals(expected, actual);
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
    public void testGetMoveableSquaresForPawnWhenPawnIsOnEdge() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.E8}, new Colour[] {Colour.WHITE});
        PiecesState state = new PiecesState(colourLocations, new HashSet<>());

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForPawn(Square.E8, Colour.WHITE, state);

        // Then
        assertEquals(new HashSet<>(), actual);
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
    public void testGetMoveableSquaresForCastle() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.D7, Square.B5, Square.D5, Square.F5},
                new Colour[] {Colour.WHITE, Colour.BLACK, Colour.WHITE, Colour.BLACK});
        Set<Square> kingLocations = CollectionUtil.createSet(new Square[] {Square.F5});
        PiecesState state = new PiecesState(colourLocations, kingLocations);

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForCastle(Square.D5, Colour.WHITE, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(
                new Square[] {Square.D6, Square.B5, Square.C5, Square.E5, Square.D4, Square.D3, Square.D2, Square.D1});
        assertEquals(expected, actual);
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
    public void testGetMoveableSquaresForKnight() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.D7, Square.B6, Square.A5, Square.B5, Square.D5, Square.A4},
                new Colour[] {Colour.WHITE, Colour.BLACK, Colour.BLACK, Colour.BLACK, Colour.BLACK, Colour.WHITE});
        Set<Square> kingLocations = CollectionUtil.createSet(new Square[] {Square.D7});
        PiecesState state = new PiecesState(colourLocations, kingLocations);

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForKnight(Square.B6, Colour.BLACK, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(new Square[] {Square.A4, Square.C4, Square.A8, Square.C8});
        assertEquals(expected, actual);
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
    public void testGetMoveableSquaresForBishop() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.B7, Square.C4, Square.D5, Square.G2},
                new Colour[] {Colour.BLACK, Colour.WHITE, Colour.WHITE, Colour.BLACK});
        Set<Square> kingLocations = CollectionUtil.createSet(new Square[] {Square.G2});
        PiecesState state = new PiecesState(colourLocations, kingLocations);

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForBishop(Square.D5, Colour.WHITE, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(
                new Square[] {Square.B7, Square.C6, Square.E6, Square.F7, Square.G8, Square.E4, Square.F3});
        assertEquals(expected, actual);
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
    public void testGetMoveableSquaresForQueen() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.D8, Square.B7, Square.F5, Square.C4, Square.D5, Square.G2},
                new Colour[] {Colour.WHITE, Colour.BLACK, Colour.BLACK, Colour.WHITE, Colour.BLACK, Colour.WHITE});
        Set<Square> kingLocations = CollectionUtil.createSet(new Square[] {Square.G2});
        PiecesState state = new PiecesState(colourLocations, kingLocations);

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForQueen(Square.D5, Colour.BLACK, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(
                new Square[] {Square.D8, Square.D7, Square.D6, Square.C6, Square.E6, Square.F7, Square.G8,
                Square.A5, Square.B5, Square.C5, Square.E5, Square.C4, Square.D4, Square.D3, Square.D2, Square.D1,
                Square.E4, Square.F3});
        assertEquals(expected, actual);
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
    public void testGetMoveableSquaresForKing() {
        // Given
        Map<Square, Colour> colourLocations = CollectionUtil.createMap(
                new Square[] {Square.C4, Square.C3}, new Colour[] {Colour.WHITE, Colour.BLACK});
        Set<Square> kingLocations = CollectionUtil.createSet(new Square[] {Square.D4});
        PiecesState state = new PiecesState(colourLocations, kingLocations);

        // When
        Set<Square> actual = MovesUtil.getMoveableSquaresForKing(Square.D4, Colour.WHITE, state);

        // Then
        Set<Square> expected = CollectionUtil.createSet(
                new Square[] {Square.C5, Square.D5, Square.E5, Square.E4, Square.C3, Square.D3, Square.E3});
        assertEquals(expected, actual);
    }
}
