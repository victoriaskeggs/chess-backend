package model.piecemover;

import model.Colour;
import model.Move;
import model.PieceType;
import model.Square;
import model.piece.PieceFactory;
import model.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import testutil.CollectionUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EndgameHelperTest {
    /**
     * Object under test
     */
    @InjectMocks
    private EndgameHelper endgameHelper;

    /**
     * Dependency in EndgameHelper
     */
    @Mock(name = "pieceFactory")
    private PieceFactory pieceFactory;

    /**
     * Dependency in EndgameHelper
     */
    @Mock(name = "pieceMover")
    private PieceMover pieceMover;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |__|BC|__|  |WK|  |__|  |
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testIsInCheckWhenInCheck() {
        // Given
        Piece blackCastle = setUpMock(PieceType.CASTLE, Colour.BLACK, Square.B1, CollectionUtil.createSet(new Square[] {
                Square.A1, Square.C1, Square.D1, Square.E1 // squares threatened by castle
        }));
        Piece whiteKing = setUpMock(PieceType.KING, Colour.WHITE, Square.E1, CollectionUtil.createSet(new Square[] {
                Square.D1, Square.F1, Square.D2, Square.E2, Square.F2 // squares threatened by king
        }));
        when(pieceMover.getPieces()).thenReturn(CollectionUtil.createSet(new Piece[] {blackCastle, whiteKing}));

        // When
        boolean isInCheck = endgameHelper.isInCheck(whiteKing,
                CollectionUtil.createSet(new Piece[] {blackCastle, whiteKing}));

        // Then
        assertTrue(isInCheck);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|BC|__|  |
     * 2: |  |__|BB|__|  |__|  |__|
     * 1: |__|  |__|  |WK|  |__|  |
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testIsInCheckWhenNotInCheck() {
        // Given
        Piece blackCastle = setUpMock(PieceType.CASTLE, Colour.BLACK, Square.F3, CollectionUtil.createSet(new Square[] {
                Square.A3, Square.B3, Square.C3, Square.D3, Square.E3, Square.G3, Square.H3, Square.F1, Square.F2
        }));
        Piece blackBishop = setUpMock(PieceType.CASTLE, Colour.BLACK, Square.C2, CollectionUtil.createSet(new Square[] {
                Square.D1, Square.B3, Square.A4, Square.B1, Square.D3
        }));
        Piece whiteKing = setUpMock(PieceType.KING, Colour.WHITE, Square.E1, CollectionUtil.createSet(new Square[] {
                Square.D1, Square.F1, Square.D2, Square.E2, Square.F2 // squares threatened by king
        }));
        when(pieceMover.getPieces()).thenReturn(CollectionUtil.createSet(new Piece[] {blackCastle, blackBishop, whiteKing}));

        // When
        boolean isInCheck = endgameHelper.isInCheck(whiteKing,
                CollectionUtil.createSet(new Piece[] {blackCastle, blackBishop, whiteKing}));

        // Then
        assertFalse(isInCheck);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |BP|  |
     * 2: |  |__|  |__|  |BC|  |__|
     * 1: |__|BC|__|  |WK|  |__|  |
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testIsInCheckmateWhenInCheckmate() {
        /**
         * Given
         */
        // Set up black castle on B1, which can threaten all the squares in row 1
        Piece blackCastle1 = setUpMock(PieceType.CASTLE, Colour.BLACK, Square.B1, CollectionUtil.createSet(new Square[] {
                Square.A1, Square.C1, Square.D1, Square.E1, Square.F1
        }));
        // Set up black castle on F2
        Piece blackCastle2 = setUpMock(PieceType.CASTLE, Colour.BLACK, Square.F2, CollectionUtil.createSet(new Square[] {
                Square.F1, Square.A2, Square.B2, Square.C2, Square.D2, Square.E2, Square.G2, Square.H2, Square.F1
        }));
        // Set up black pawn on G3, which can threaten/protect squares on a southern diagonal
        Piece blackPawn = setUpMock(PieceType.PAWN, Colour.BLACK, Square.G3, CollectionUtil.createSet(new Square[] {
                Square.H2, Square.F2
        }));
        // Set up white king on E1
        Piece whiteKing = setUpMock(PieceType.KING, Colour.WHITE, Square.E1, CollectionUtil.createSet(new Square[] {
                Square.D1, Square.F1, Square.D2, Square.E2, Square.F2
        }));
        // Set up piece mover to return these pieces
        when(pieceMover.getPieces()).thenReturn(CollectionUtil.createSet(new Piece[] {blackCastle1, blackCastle2, blackPawn, whiteKing}));

        // Set up king to return its current square after it is moved
        rememberPieceMove(whiteKing, PieceType.KING, Colour.WHITE, Square.E1);

        /**
         * When
         */
        boolean isInCheckmate = endgameHelper.isInCheckmate(whiteKing,
                CollectionUtil.createSet(new Piece[] {blackCastle1, blackCastle2, blackPawn, whiteKing}));

        /**
         * Then
         */
        assertTrue(isInCheckmate);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |BB|  |__|  |__|
     * 3: |__|  |__|  |__|  |BH|  |
     * 2: |  |__|  |BP|  |__|  |__|
     * 1: |__|BC|__|  |WK|  |__|  |
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testIsInCheckmateWhenNotInCheckmate() {
        /**
         * Given
         */
        // Set up black castle on B1, which can threaten all the squares in row 1
        Piece blackCastle = setUpMock(PieceType.CASTLE, Colour.BLACK, Square.B1, CollectionUtil.createSet(new Square[] {
                Square.A1, Square.C1, Square.D1, Square.E1, Square.F1
        }));
        // Set up black bishop on D4
        Piece blackBishop = setUpMock(PieceType.BISHOP, Colour.BLACK, Square.D4, CollectionUtil.createSet(new Square[] {
                Square.E3, Square.F2, Square.G1, Square.C3, Square.B2, Square.A1
        }));
        // Set up black pawn on D2, which can threaten/protect squares on a southern diagonal
        Piece blackPawn = setUpMock(PieceType.PAWN, Colour.BLACK, Square.D2, CollectionUtil.createSet(new Square[] {
                Square.E1, Square.C1
        }));
        // Set up black knight on G3
        Piece blackKnight = setUpMock(PieceType.KNIGHT, Colour.BLACK, Square.G3, CollectionUtil.createSet(new Square[] {
                Square.F1, Square.E2
        }));
        // Set up white king on E1
        Piece whiteKing = setUpMock(PieceType.KING, Colour.WHITE, Square.E1, CollectionUtil.createSet(new Square[] {
                Square.D1, Square.F1, Square.D2, Square.E2, Square.F2
        }));

        // Set up piece mover to return these pieces
        when(pieceMover.getPieces()).thenReturn(CollectionUtil.createSet(new Piece[] {blackCastle, blackBishop, blackPawn, blackKnight, whiteKing}));

        // Set up king to return its current square after it is moved
        rememberPieceMove(whiteKing, PieceType.KING, Colour.WHITE, Square.E1);

        /**
         * When
         */
        boolean isInCheckmate = endgameHelper.isInCheckmate(whiteKing,
                CollectionUtil.createSet(new Piece[] {blackCastle, blackBishop, blackPawn, blackKnight, whiteKing}));

        /**
         * Then
         */
        assertFalse(isInCheckmate);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |__|  |__|  |__|  |__|
     * 3: |__|BQ|__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |WK|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testIsInStalemateWhenInStalemate() {
        /**
         * Given
         */
        // Set up black queen on B3
        Piece blackQueen = setUpMock(PieceType.QUEEN, Colour.BLACK, Square.B3, CollectionUtil.createSet(new Square[] {
                Square.B2, Square.B1, Square.A2, Square.A3
        }));
        // Set up white king on E1
        Piece whiteKing = setUpMock(PieceType.KING, Colour.WHITE, Square.A1, CollectionUtil.createSet(new Square[] {
                Square.A2, Square.B2, Square.B1
        }));

        // Set up piece mover to return these pieces
        when(pieceMover.getPieces()).thenReturn(CollectionUtil.createSet(new Piece[] {blackQueen, whiteKing}));

        // Set up king to return its current square after it is moved
        rememberPieceMove(whiteKing, PieceType.KING, Colour.WHITE, Square.A1);

        /**
         * When
         */
        boolean isInCheckmate = endgameHelper.isInStalemate(whiteKing,
                CollectionUtil.createSet(new Piece[] {blackQueen, whiteKing}));

        /**
         * Then
         */
        assertTrue(isInCheckmate);
    }

    /**
     * 8: |  |__|  |__|  |__|  |__|
     * 7: |__|  |__|  |__|  |__|  |
     * 6: |  |__|  |__|  |__|  |__|
     * 5: |__|  |__|  |__|  |__|  |
     * 4: |  |BQ|  |__|  |__|  |__|
     * 3: |__|  |__|  |__|  |__|  |
     * 2: |  |__|  |__|  |__|  |__|
     * 1: |WK|  |__|  |__|  |__|  |
     *     A  B  C  D  E  F  G  H
     */
    @Test
    public void testIsInStalemateWhenNotInStalemate() {
        /**
         * Given
         */
        // Set up black queen on B3
        Piece blackQueen = setUpMock(PieceType.QUEEN, Colour.BLACK, Square.B4, CollectionUtil.createSet(new Square[] {
                Square.B3, Square.B2, Square.B1, Square.A4, Square.A3
        }));
        // Set up white king on E1
        Piece whiteKing = setUpMock(PieceType.KING, Colour.WHITE, Square.A1, CollectionUtil.createSet(new Square[] {
                Square.A2, Square.B2, Square.B1
        }));

        // Set up piece mover to return these pieces
        when(pieceMover.getPieces()).thenReturn(CollectionUtil.createSet(new Piece[] {blackQueen, whiteKing}));

        // Set up king to return its current square after it is moved
        rememberPieceMove(whiteKing, PieceType.KING, Colour.WHITE, Square.A1);

        /**
         * When
         */
        boolean isInCheckmate = endgameHelper.isInStalemate(whiteKing,
                CollectionUtil.createSet(new Piece[] {blackQueen, whiteKing}));

        /**
         * Then
         */
        assertFalse(isInCheckmate);
    }

    /**
     * Sets up a mock piece to return values when getType(), getColour(), getCurrentSquare(), getMoveableSquares()
     * and doesThreaten(Square) are called on the piece.
     * @param type to return when getType() is called
     * @param colour to return get getColour() is called
     * @param currentSquare to return when getCurrentSquare() is called
     * @param threatenedSquares to return when getThreatenedSquares() is called
     */
    private Piece setUpMock(PieceType type, Colour colour, Square currentSquare, Set<Square> threatenedSquares) {
        Piece piece = mock(Piece.class);

        // Set up basic piece characteristics
        when(piece.getType()).thenReturn(type);
        when(piece.getColour()).thenReturn(colour);
        when(piece.getCurrentSquare()).thenReturn(currentSquare);

        // Set up threatened squares
        when(piece.getThreatenedSquares()).thenReturn(threatenedSquares);
        for (Square square : threatenedSquares) {
            when(piece.doesThreaten(square)).thenReturn(true);
        }

        // Set up moveable squares if piece is a king: assume moveable squares are threatened squares
        if (type == PieceType.KING) {
            when(piece.getMoveableSquares()).thenReturn(threatenedSquares);
            for (Square square : threatenedSquares) {
                when(piece.canMoveTo(square)).thenReturn(true);
            }
        }
        return piece;
    }

    /**
     * Stores the square a piece is asked to move to when the PieceMover is asked to move the piece via
     * PieceMover.move(). Returns this new square as the piece's current location. Resets the piece's location to its
     * initial square when pieceMover.undoMove() is called.
     * @param piece
     * @param type
     * @param colour
     * @param initialLocation
     */
    private void rememberPieceMove(Piece piece, PieceType type, Colour colour, Square initialLocation) {
        // Set up pieceMover.move() to store where piece has been moved to
        final Square[] square = {initialLocation};
        doAnswer(invocation -> {
            Move move = invocation.getArgument(0);
            if (move.getPieceType() == type && move.getColour() == colour) {
                square[0] = move.getTo();
            }
            return null;
        }).when(pieceMover).move(any());

        // Set up pieceMover.undoMove() to reset where piece has been moved to
        doAnswer(invocation -> {
            square[0] = Square.E1;
            return null;
        }).when(pieceMover).undoMove();

        // Set up piece.getCurrentSquare() to return the stored value of where the piece has been moved to
        when(piece.getCurrentSquare()).thenAnswer((Answer<Square>) invocation -> square[0]);
    }
}