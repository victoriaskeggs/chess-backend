package model.pieces;

import model.Move;
import model.Square;
import model.piece.Piece;
import model.piece.PieceState;

import java.util.Set;

public class EndgameHelper {
    /**
     * Makes test moves in a trial environment to check endgame conditions
     */
    private PiecesMover piecesMover;

    public EndgameHelper() {
        piecesMover = new PiecesMover();
    }

    /**
     * Checks whether a king is under check in the current state of the board.
     * @param kingState
     * @param piecesState
     * @return true if the given king is under check
     */
    public boolean isInCheck(PieceState kingState, PiecesState piecesState) {
        // Sets up PieceMover to control the provided pieces
        piecesMover.clearPieces();
        piecesMover.addPieces(piecesState);

        Set<Piece> pieces = piecesMover.getPieces();
        Piece king = piecesMover.findPiece(kingState);

        return isInCheck(king, pieces);
    }

    /**
     * Checks whether a king is under check in the current state of the board.
     * @param king
     * @param pieces
     * @return true if the given king is under check
     */
    private boolean isInCheck(Piece king, Set<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getState().getColour() != king.getState().getColour() &&
                    piece.doesThreaten(king.getState().getSquare())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a team has any moves they can make, such that they will not be in check at the end of the move
     * @param kingState of team
     */
    private boolean canMove(PieceState kingState, PiecesState piecesState) {
        // Sets up PieceMover to control the provided pieces
        piecesMover.clearPieces();
        piecesMover.addPieces(piecesState);

        Set<Piece> pieces = piecesMover.getPieces();
        Piece king = piecesMover.findPiece(kingState);

        for (Piece piece : pieces) {
            if (piece.getState().getColour() == king.getState().getColour()) {
                for (Square moveableSquare : piece.getMoveableSquares()) {

                    // Perform move
                    Move move = new Move(piece.getState(), moveableSquare);
                    piecesMover.move(move);

                    // Check if move resulted in check
                    boolean isChecked = isInCheck(king, pieces);

                    // Undo move
                    piecesMover.undoMove();

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
     * @param kingState
     * @param piecesState
     * @return true if the given team is checkmated
     */
    public boolean isInCheckmate(PieceState kingState, PiecesState piecesState) {
        // Check that the king is currently in check
        if (!isInCheck(kingState, piecesState)) {
            return false;
        }
        // Check whether there is a move the team can make that will get them out of check
        if (canMove(kingState, piecesState)) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a given team is stalemated in the current state of the board. Checks if either:
     * - there is nowhere the given team can move
     * - there are not enough pieces on the board for either team to win
     * @param kingState
     * @param piecesState
     * @return true if the given team is stalemated
     */
    public boolean isInStalemate(PieceState kingState, PiecesState piecesState) {
        // Check that the king is not in check
        if (isInCheck(kingState, piecesState)) {
            return false;
        }
        // For all possible moves the team can make, check all of them will put the team in check
        if (canMove(kingState, piecesState)) {
            return false;
        }
        // Check that there are enough pieces for one side to checkmate
        // TODO
        return true;
    }
}
