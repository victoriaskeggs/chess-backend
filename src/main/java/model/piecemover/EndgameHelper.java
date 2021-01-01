package model.piecemover;

import model.Move;
import model.Square;
import model.piece.Piece;
import model.piece.PieceFactory;

import java.util.HashMap;
import java.util.Set;

public class EndgameHelper {
    /**
     * Factory to generate new pieces for checking endgame conditions
     */
    private PieceFactory pieceFactory;

    /**
     * Makes test moves in a trial environment to check endgame conditions
     */
    private PieceMover pieceMover;

    public EndgameHelper() {
        pieceFactory = new PieceFactory();
        pieceMover = new PieceMover(new HashMap<>());
    }

    /**
     * Checks whether a king is under check in the current state of the board.
     * @param king
     * @param pieces on the board
     * @return true if the given king is under check
     */
    public boolean isInCheck(Piece king, Set<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getColour() != king.getColour() && piece.doesThreaten(king.getCurrentSquare())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a team has any moves they can make, such that they will not be in check at the end of the move
     * @param king of team
     */
    private boolean canMove(Piece king, Set<Piece> pieces) {
        // Sets up PieceMover to control the provided pieces
        pieceMover.clearPieces();
        pieceMover.addPieces(pieceFactory.createCopyOfPieces(pieces));

        for (Piece piece : pieceMover.getPieces()) {
            if (piece.getColour() == king.getColour()) {
                for (Square moveableSquare : piece.getMoveableSquares()) {

                    // Perform move
                    Move move = new Move(piece.getType(), piece.getColour(), piece.getCurrentSquare(), moveableSquare);
                    pieceMover.move(move);

                    // Check if move resulted in check
                    boolean isChecked = isInCheck(king, pieces);

                    // Undo move
                    pieceMover.undoMove();

                    // If move did not result in check, the team has an available move
                    if (!isChecked) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks whether a given team is checkmated in the current state of the board.
     * @param king
     * @param pieces on the board
     * @return true if the given team is checkmated
     */
    public boolean isInCheckmate(Piece king, Set<Piece> pieces) {
        // Check that the king is currently in check
        if (!isInCheck(king, pieces)) {
            return false;
        }
        // Check whether there is a move the team can make that will get them out of check
        if (canMove(king, pieces)) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a given team is stalemated in the current state of the board. Checks if either:
     * - there is nowhere the given team can move
     * - there are not enough pieces on the board for either team to win
     * @param king
     * @param pieces on the board
     * @return true if the given team is stalemated
     */
    public boolean isInStalemate(Piece king, Set<Piece> pieces) {
        // Check that the king is not in check
        if (isInCheck(king, pieces)) {
            return false;
        }
        // For all possible moves the team can make, check all of them will put the team in check
        if (canMove(king, pieces)) {
            return false;
        }
        // Check that there are enough pieces for one side to checkmate
        // TODO
        return true;
    }
}
