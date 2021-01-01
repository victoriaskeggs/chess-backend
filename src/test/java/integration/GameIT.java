package integration;

import model.Colour;
import model.Move;
import model.PieceType;
import model.Square;
import model.game.Game;
import model.game.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameIT {

    /**
     * White performs four move checkmate and black makes non-interfering moves:
     *
     * WP: E2 to E3
     * BP: A7 to A6
     * WB: F1 to C4
     * BP: H7 to H6
     * WQ: D1 to F3
     * BC: H8 to H7
     * WQ: F3 to F7
     *
     * 8: |BC|BH|BB|BQ|BK|BB|BH|BC|
     * 7: |BP|BP|BP|BP|BP|BP|BP|BP|
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |WP|WP|WP|WP|WP|WP|WP|WP|
     * 1: |WC|WH|WB|WQ|WK|WB|WH|WC|
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testStartToCheckmate() {
        Game game = new Game();

        // White pawn moves from E2 to E3
        GameState state = game.move(new Move(PieceType.PAWN, Colour.WHITE, Square.E2, Square.E3));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn moves from A7 to A6
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.A7, Square.A6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White bishop moves from F1 to C4
        state = game.move(new Move(PieceType.BISHOP, Colour.WHITE, Square.F1, Square.C4));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn moves from H7 to H6
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.H7, Square.H6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen moves from D1 to F3
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.D1, Square.F3));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black castle moves from H8 to H7
        state = game.move(new Move(PieceType.CASTLE, Colour.BLACK, Square.H8, Square.H7));
        assertEquals(GameState.IN_PROGRESS, state);

        // TODO queen was killed instead of pawn as queen sees a black piece on F7 and thinks it's been killed
        // White queen moves from F3 to F7
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.F3, Square.F7));
        assertEquals(GameState.OVER_CHECKMATE, state);
    }

    /**
     * White and black reach stalemate:
     *
     * WP: E2 to E3
     * BP: A7 to A6
     * WH: G1 to H3
     * BP: A6 to A5
     * WQ: D1 to H5
     * BC: A8 to A6
     * WQ: H5 to A5
     * BP: H7 to H6
     * WH: H3 to G1
     * BP: H6 to H5
     * WQ: H5 to C7
     * BC: A6 to H6
     * WH: G1 to H3
     * BP: F7 to H6
     * WQ: C7 to D7
     * BK: E8 to F7
     * WQ: D7 to B7
     * BQ: D8 to D3
     * WQ: B7 to B8
     * BQ: D3 to H7
     * WQ: B8 to C8
     * BK: F7 to G6
     * WQ: C8 to E6
     *
     * 8: |BC|BH|BB|BQ|BK|BB|BH|BC|
     * 7: |BP|BP|BP|BP|BP|BP|BP|BP|
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |WP|WP|WP|WP|WP|WP|WP|WP|
     * 1: |WC|WH|WB|WQ|WK|WB|WH|WC|
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testStartToStalemate() {
        Game game = new Game();

        // White pawn from E2 to E3
        GameState state = game.move(new Move(PieceType.PAWN, Colour.WHITE, Square.E2, Square.E3));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn from A7 to A6
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.A7, Square.A6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White knight from G1 to H3
        state = game.move(new Move(PieceType.KNIGHT, Colour.WHITE, Square.G1, Square.H3));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn from A6 to A5
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.A6, Square.A5));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from D1 to H5
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.D1, Square.H5));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black castle from A8 to A6
        state = game.move(new Move(PieceType.CASTLE, Colour.BLACK, Square.A8, Square.A6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from H5 to A5
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.H5, Square.A5));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn from H7 to H6
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.H7, Square.H6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White knight from H3 to G1
        state = game.move(new Move(PieceType.KNIGHT, Colour.WHITE, Square.H3, Square.G1));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn from H6 to H5
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.H6, Square.H5));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from H5 to C7
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.H5, Square.C7));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black castle from A6 to H6
        state = game.move(new Move(PieceType.CASTLE, Colour.BLACK, Square.A6, Square.H6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White knight from G1 to H3
        state = game.move(new Move(PieceType.KNIGHT, Colour.WHITE, Square.G1, Square.H3));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black pawn from F7 to H6
        state = game.move(new Move(PieceType.PAWN, Colour.BLACK, Square.F7, Square.H6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from C7 to D7 - check
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.C7, Square.D7));
        assertEquals(GameState.IN_PROGRESS_CHECK, state);

        // Black king from E8 to F7
        state = game.move(new Move(PieceType.KING, Colour.BLACK, Square.E8, Square.F7));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from D7 to B7
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.D7, Square.B7));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black queen from D8 to D3
        state = game.move(new Move(PieceType.QUEEN, Colour.BLACK, Square.D8, Square.D3));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from B7 to B8
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.B7, Square.B8));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black queen from D3 to H7
        state = game.move(new Move(PieceType.QUEEN, Colour.BLACK, Square.D3, Square.H7));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from B8 to C8
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.B8, Square.C8));
        assertEquals(GameState.IN_PROGRESS, state);

        // Black king from F7 to G6
        state = game.move(new Move(PieceType.KING, Colour.BLACK, Square.F7, Square.G6));
        assertEquals(GameState.IN_PROGRESS, state);

        // White queen from C8 to E6
        state = game.move(new Move(PieceType.QUEEN, Colour.WHITE, Square.C8, Square.E6));
        assertEquals(GameState.OVER_STALEMATE, state);
    }
}
