package model.piece;

import model.Colour;
import model.Square;
import model.PieceType;
import util.CollectionUtil;

import java.util.*;

/**
 * Factory for creating chess pieces
 */
public class PieceFactory {

    /**
     * Creates all the chess pieces in their default starting locations.
     * @return default pieces in their starting locations, mapped by their type
     */
    public Map<PieceType, Set<Piece>> createDefaultPieces() {

        Map<PieceType, Set<Piece>> piecesByType = new HashMap<>();

        // Set up kings
        Set<Piece> kings = new HashSet<>(Arrays.asList(
                new King(Colour.BLACK, Square.E8), new King(Colour.WHITE, Square.E1)));
        piecesByType.put(PieceType.KING, kings);

        // Set up queens
        Set<Piece> queens = new HashSet<>(Arrays.asList(
                new Queen(Colour.BLACK, Square.D8), new Queen(Colour.WHITE, Square.D1)));
        piecesByType.put(PieceType.QUEEN, queens);

        // Set up bishops
        Set<Piece> bishops = new HashSet<>(Arrays.asList(
                new Bishop(Colour.BLACK, Square.C8), new Bishop(Colour.BLACK, Square.F8),
                new Bishop(Colour.WHITE, Square.C1), new Bishop(Colour.WHITE, Square.F1)));
        piecesByType.put(PieceType.BISHOP, bishops);

        // Set up knights
        Set<Piece> knights = new HashSet<>(Arrays.asList(
                new Knight(Colour.BLACK, Square.B8), new Knight(Colour.BLACK, Square.G8),
                new Knight(Colour.WHITE, Square.B1), new Knight(Colour.WHITE, Square.G1)));
        piecesByType.put(PieceType.KNIGHT, knights);

        // Set up castles
        Set<Piece> castles = new HashSet<>(Arrays.asList(
                new Castle(Colour.BLACK, Square.A8), new Castle(Colour.BLACK, Square.H8),
                new Castle(Colour.WHITE, Square.A1), new Castle(Colour.WHITE, Square.H1)));
        piecesByType.put(PieceType.CASTLE, castles);

        // Set up pawns
        Set<Piece> pawns = new HashSet<>(Arrays.asList(
                new Pawn(Colour.BLACK, Square.A7), new Pawn(Colour.BLACK, Square.B7),
                new Pawn(Colour.BLACK, Square.C7), new Pawn(Colour.BLACK, Square.D7),
                new Pawn(Colour.BLACK, Square.E7), new Pawn(Colour.BLACK, Square.F7),
                new Pawn(Colour.BLACK, Square.G7), new Pawn(Colour.BLACK, Square.H7),
                new Pawn(Colour.WHITE, Square.A2), new Pawn(Colour.WHITE, Square.B2),
                new Pawn(Colour.WHITE, Square.C2), new Pawn(Colour.WHITE, Square.D2),
                new Pawn(Colour.WHITE, Square.E2), new Pawn(Colour.WHITE, Square.F2),
                new Pawn(Colour.WHITE, Square.G2), new Pawn(Colour.WHITE, Square.H2)));
        piecesByType.put(PieceType.PAWN, pawns);

        return piecesByType;
    }

    /**
     * Creates a deep copy of the provided chess pieces
     * @param pieces
     * @return copied pieces, mapped by their type
     */
    public Map<PieceType, Set<Piece>> createCopyOfPieces(Set<Piece> pieces) {
        Map<PieceType, Set<Piece>> piecesByType = new HashMap<>();
        for (Piece piece : pieces) {
            Piece copiedPiece = createCopyOf(piece);
            CollectionUtil.addToMap(piecesByType, copiedPiece.getType(), copiedPiece);
        }
        return piecesByType;
    }

    /**
     * Creates a deep copy of the provided piece.
     * @param piece
     * @return copied piece
     */
    private Piece createCopyOf(Piece piece) {
        switch (piece.getType()) {
            case PAWN:
                return new Pawn(piece.getColour(), piece.getCurrentSquare());
            case CASTLE:
                return new Castle(piece.getColour(), piece.getCurrentSquare());
            case KNIGHT:
                return new Knight(piece.getColour(), piece.getCurrentSquare());
            case BISHOP:
                return new Bishop(piece.getColour(), piece.getCurrentSquare());
            case QUEEN:
                return new Queen(piece.getColour(), piece.getCurrentSquare());
            case KING:
                return new King(piece.getColour(), piece.getCurrentSquare());
            default:
                throw new RuntimeException(); // TODO log and msg
        }
    }
}
