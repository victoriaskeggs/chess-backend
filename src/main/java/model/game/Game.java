package model.game;

import model.Colour;
import model.Move;
import model.exception.ChessException;
import model.pieces.Board;
import model.pieces.PiecesState;

public class Game {

    private GameStatus status;
    private Board board;
    private Colour turn;

    /**
     * Creates a new game
     */
    public Game() {
        status = GameStatus.IN_PROGRESS;
        board = new Board();
        turn = Colour.WHITE;
    }

    /**
     * Creates a game already in progress
     */
    public Game(GameStatus status, Board board, Colour turn) {
        this.status = status;
        this.board = board;
        this.turn = turn;
    }

    /**
     * Makes a move if allowed
     * @param move
     * @return new state of the game
     * @throws ChessException if move is not allowed
     */
    public GameState move(Move move) {
        // Pre-move validation
        validateGameInProgress();
        validateIsColoursTurn(move.getPieceState().getColour());

        // Make the move
        PiecesState boardState = board.move(move);

        // Post-move validation
        validateCurrentColourNotInCheck();

        // Update game
        updateGameStatus();
        turn = getOpponent(move.getPieceState().getColour());

        return new GameState(status, boardState);
    }

    /**
     * @throws ChessException if a game is not in progress
     */
    private void validateGameInProgress() {
        if (status == GameStatus.OVER_STALEMATE || status == GameStatus.OVER_CHECKMATE) {
            throw new ChessException("Cannot make a move as game is over."); // TODO log
        }
    }

    /**
     * @param colour
     * @throws ChessException if it is not the provided colour's turn
     */
    private void validateIsColoursTurn(Colour colour) {
        if (colour != turn) {
            throw new ChessException(String.format("Colour %s cannot move as it is not their turn.", colour)); // TODO log
        }
    }

    /**
     * @throws ChessException if the current colour is in check
     */
    private void validateCurrentColourNotInCheck() {
        if (board.isChecked(turn)) {
            throw new ChessException(String.format("Colour %s cannot be in check after their move.", turn)); // TODO log
        }
    }

    /**
     * Checks if the board is in a state of check, checkmate or stalemate and updates the game state
     * accordingly
     */
    private void updateGameStatus() {
        Colour opponent = getOpponent(turn);
        if (board.isCheckmated(opponent)) {
            status = GameStatus.OVER_CHECKMATE;
            return;
        }
        if (board.isStalemated(opponent)) {
            status = GameStatus.OVER_STALEMATE;
            return;
        }
        if (board.isChecked(opponent)) {
            status = GameStatus.IN_PROGRESS_CHECK;
            return;
        }
        status = GameStatus.IN_PROGRESS;
    }

    /**
     * Retrieves the given colour's opponent colour
     * @param colour
     * @return opponent colour
     */
    private Colour getOpponent(Colour colour) {
        switch (colour) {
            case WHITE:
                return Colour.BLACK;
            case BLACK:
                return Colour.WHITE;
        }
        throw new RuntimeException(); // TODO log and msg - should not be reachable
    }
}
