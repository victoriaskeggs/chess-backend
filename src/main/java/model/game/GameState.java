package model.game;

import model.pieces.PiecesState;

/**
 * Immutable state of a chess game, including board state and the status of the game, ie. over.
 */
public class GameState {

    /**
     *  Status of the game
     */
    private final GameStatus status;

    /**
     * State of the game board
     */
    private final PiecesState state;

    /**
     * Creates an immutable representation of a chess game
     * @param status of the game
     * @param state of the game board
     */
    public GameState(GameStatus status, PiecesState state) {
        this.status = status;
        this.state = state;
    }

    /**
     * @return status of the game
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * @return state of the game board
     */
    public PiecesState getState() {
        return state;
    }
}
