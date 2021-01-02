package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import model.pieces.PiecesState;

import java.util.*;

import static model.PieceType.*;

/**
 * Factory for creating chess pieces
 */
public class PieceFactory {

    /**
     * Creates the starting state of the chess board, including all the pieces in their starting locations
     * @return default pieces in their starting locations
     */
    public PiecesState createDefaultPiecesState() {
        Set<PieceState> pieceStates = new HashSet<>();

        // Set up kings
        pieceStates.addAll(Arrays.asList(
                new PieceState(KING, Colour.BLACK, Square.E8),
                new PieceState(PieceType.KING, Colour.WHITE, Square.E1)));

        // Set up queens
        pieceStates.addAll(Arrays.asList(
                new PieceState(PieceType.QUEEN, Colour.BLACK, Square.D8),
                new PieceState(PieceType.QUEEN, Colour.WHITE, Square.D1)));

        // Set up bishops
        pieceStates.addAll(Arrays.asList(
                new PieceState(PieceType.BISHOP, Colour.BLACK, Square.C8),
                new PieceState(PieceType.BISHOP, Colour.BLACK, Square.F8),
                new PieceState(PieceType.BISHOP, Colour.WHITE, Square.C1),
                new PieceState(PieceType.BISHOP, Colour.WHITE, Square.F1)));

        // Set up knights
        pieceStates.addAll(Arrays.asList(
                new PieceState(PieceType.KNIGHT, Colour.BLACK, Square.B8),
                new PieceState(PieceType.KNIGHT, Colour.BLACK, Square.G8),
                new PieceState(PieceType.KNIGHT, Colour.WHITE, Square.B1),
                new PieceState(PieceType.KNIGHT, Colour.WHITE, Square.G1)));

        // Set up castles
        pieceStates.addAll(Arrays.asList(
                new PieceState(PieceType.CASTLE, Colour.BLACK, Square.A8),
                new PieceState(PieceType.CASTLE, Colour.BLACK, Square.H8),
                new PieceState(PieceType.CASTLE, Colour.WHITE, Square.A1),
                new PieceState(PieceType.CASTLE, Colour.WHITE, Square.H1)));

        // Set up pawns
        pieceStates.addAll(Arrays.asList(
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.A7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.B7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.C7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.D7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.E7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.F7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.G7),
                new PieceState(PieceType.PAWN, Colour.BLACK, Square.H7),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.A2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.B2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.C2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.D2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.E2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.F2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.G2),
                new PieceState(PieceType.PAWN, Colour.WHITE, Square.H2)));

        return new PiecesState(pieceStates);
    }

    /**
     * Generates a set of Pieces in the provided state
     * @param piecesState
     * @return generated Pieces
     */
    public Set<Piece> createPieces(PiecesState piecesState) {
        Set<Piece> pieces = new HashSet<>();
        for (PieceState pieceState : piecesState.getPieceStates()) {
            pieces.add(createPiece(pieceState));
        }
        return pieces;
    }

    /**
     * Generates a Piece in the provided state
     * @param state
     * @return generated Piece
     */
    public Piece createPiece(PieceState state) {
        switch (state.getType()) {
            case PAWN:
                return new Pawn(state.getColour(), state.getSquare());
            case CASTLE:
                return new Castle(state.getColour(), state.getSquare());
            case KNIGHT:
                return new Knight(state.getColour(), state.getSquare());
            case BISHOP:
                return new Bishop(state.getColour(), state.getSquare());
            case QUEEN:
                return new Queen(state.getColour(), state.getSquare());
            case KING:
                return new King(state.getColour(), state.getSquare());
            default:
                throw new RuntimeException(); // TODO log and msg
        }
    }
}
