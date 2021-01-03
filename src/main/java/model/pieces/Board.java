package model.pieces;

import model.Colour;
import model.Move;
import model.exception.ChessException;
import model.piece.Piece;
import model.piece.PieceFactory;
import model.PieceType;

import java.util.Set;

public class Board {

    /**
     * Controls moving pieces on this board.
     */
    private PiecesMover piecesMover;

    /**
     * Figures out whether the game has ended or a king is under check
     */
    private EndgameHelper endgameHelper;

    /**
     * Sets up a new game of chess.
     */
    public Board() {
        endgameHelper = new EndgameHelper();
        piecesMover = new PiecesMover(new PieceFactory().createDefaultPiecesState());
    }

    /**
     * Performs the provided move if allowed
     * @param move
     * @return new state of the board
     */
    public PiecesState move(Move move) {
        return piecesMover.move(move);
    }

    /**
     * Finds the living piece of the given colour and type.
     * @param type
     * @param colour
     * @throws ChessException if there is not exactly one living piece of the given type and colour
     * @return
     */
    private Piece findPiece(PieceType type, Colour colour) {
        Set<Piece> candidates = piecesMover.findPieces(type, colour);
        if (candidates.size() == 1) {
            return candidates.iterator().next();
        }
        throw new ChessException(String.format("There is not exactly one %s %s on the board.", colour, type)); // TODO log
    }

    /**
     * Checks whether a given team is under check in the current state of the board.
     * @param colour of the team
     * @return true if the given team is under check
     */
    public boolean isChecked(Colour colour) {
        Piece king = findPiece(PieceType.KING, colour);
        return endgameHelper.isInCheck(king.getState(), piecesMover.generatePiecesState());
    }

    /**
     * Checks whether a given team is checkmated in the current state of the board.
     * @param colour of the team
     * @return true if the given team is checkmated
     */
    public boolean isCheckmated(Colour colour) {
        Piece king = findPiece(PieceType.KING, colour);
        return endgameHelper.isInCheckmate(king.getState(), piecesMover.generatePiecesState());
    }

    /**
     * Checks whether a given team is stalemated in the current state of the board.
     * @param colour of the team
     * @return true if the given team is stalemated
     */
    public boolean isStalemated(Colour colour) {
        Piece king = findPiece(PieceType.KING, colour);
        return endgameHelper.isInStalemate(king.getState(), piecesMover.generatePiecesState());
    }
}
