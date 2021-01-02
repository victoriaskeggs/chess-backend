package model.util;

import model.Colour;
import model.PieceType;
import model.Square;
import model.piece.PieceState;
import model.pieces.PiecesState;
import util.CollectionUtil;

import java.util.*;
import java.util.function.Function;

/**
 * Utility class for calculating which squares a piece can move to.
 */
public class MovesCalculator {

    private Set<Square> moveableSquares;
    private Set<Square> threatenedSquares;
    private HashMap<Square, Colour> colourLocations;
    private Set<Square> kingLocations;

    /**
     * Whether a piece is available for a piece to move into.
     */
    private enum SquareAvailability {
        EMPTY, // no piece in the square
        AVAILABLE, // piece in the square that can be taken
        BLOCKED // piece in the square that cannot be taken
    }

    public MovesCalculator() {
        reset();
    }

    /**
     * Checks if a square with a given row and column number is inside the chess board.
     * @param row
     * @param column
     * @return true if square is inside the board
     */
    private boolean isInsideGrid(int row, int column) {
        return row > 0 && row <= Square.NUM_ROWS && column > 0 && column <= Square.NUM_SQUARES_IN_ROW;
    }

    /**
     * Retrieves a square with the given row and column if it is inside the grid.
     * @param row
     * @param column
     * @return an optional containing the square if it exists, an empty optional otherwise
     */
    private Optional<Square> getSquare(int row, int column) {
        if (isInsideGrid(row, column)) {
            Square north = Square.byPosition(row, column);
            return Optional.of(north);
        }
        return Optional.empty();
    }

    /**
     * Retrieves the square directly north of the current square, where row 1 is south and row 8 is north.
     * @param square
     * @return an optional containing the northern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getNorth(Square square) {
        return getSquare(square.getRowNumber()+1, square.getLetterNumber());
    }

    /**
     * Retrieves the square directly south of the current square, where row 1 is south and row 8 is noth.
     * @param square
     * @return an optional containing the southern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getSouth(Square square) {
        return getSquare(square.getRowNumber()-1, square.getLetterNumber());
    }

    /**
     * Retrieves the square directly east of the current square, where column A is west and column H is east.
     * @param square
     * @return an optional containing the eastern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getEast(Square square) {
        return getSquare(square.getRowNumber(), square.getLetterNumber()+1);
    }

    /**
     * Retrieves the square directly west of the current square, where column A is west and column H is east.
     * @param square
     * @return an optional containing the western square if it exists, otherwise an empty optional
     */
    private Optional<Square> getWest(Square square) {
        return getSquare(square.getRowNumber(), square.getLetterNumber()-1);
    }

    /**
     * Retrieves the square directly northeast of the current square, where H8 is the most
     * northeastern square
     * @param square
     * @return an optional containing the northeastern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getNorthEast(Square square) {
        return getSquare(square.getRowNumber()+1, square.getLetterNumber()+1);
    }

    /**
     * Retrieves the square directly northwest of the current square, where A8 is the most
     * northwestern square
     * @param square
     * @return an optional containing the northwestern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getNorthWest(Square square) {
        return getSquare(square.getRowNumber()+1, square.getLetterNumber()-1);
    }

    /**
     * Retrieves the square directly southeast of the current square, where H1 is the most
     * southeastern square
     * @param square
     * @return an optional containing the southeastern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getSouthEast(Square square) {
        return getSquare(square.getRowNumber()-1, square.getLetterNumber()+1);
    }

    /**
     * Retrieves the square directly southwest of the current square, where A1 is the most
     * southwestern square
     * @param square
     * @return an optional containing the southwestern square if it exists, otherwise an empty optional
     */
    private Optional<Square> getSouthWest(Square square) {
        return getSquare(square.getRowNumber()-1, square.getLetterNumber()-1);
    }

    /**
     * Checks if a square is available for a piece of the given colour to move into
     * @param square
     * @param colour
     * @return availability of the square
     */
    private SquareAvailability evaluateSquareAvailability(Square square, Colour colour) {
        // If the square is not occupied, it is empty
        if (!colourLocations.containsKey(square)) {
            return SquareAvailability.EMPTY;
        }
        // If the square is occupied by a king, it is blocked
        if (kingLocations.contains(square)) {
            return SquareAvailability.BLOCKED;
        }
        // If the square is occupied by a piece (not a king) of the same colour, it is also blocked
        if (colourLocations.get(square) == colour) {
            return SquareAvailability.BLOCKED;
        }
        // Otherwise the square is occupied by a piece of a different colour and therefore available
        return SquareAvailability.AVAILABLE;
    }

    /**
     * Filters the candidate squares to those that have an allowed availability.
     * @param candidates to filter
     * @param colour of piece hoping to move to the square
     * @param allowed allowed availabilities
     * @return filtered set
     */
    private Set<Square> filterByAvailability(
            Set<Square> candidates, Colour colour, SquareAvailability... allowed) {
        // Make allowed availabilities easy to search
        Set<SquareAvailability> availabilities = new HashSet<>(Arrays.asList(allowed));

        // Filter squares
        Set<Square> filtered = new HashSet<>();
        for (Square candidate : candidates) {
            SquareAvailability availability = evaluateSquareAvailability(candidate, colour);
            if (availabilities.contains(availability)) {
                filtered.add(candidate);
            }
        }
        return filtered;
    }

    /**
     * Retrieves the squares diagonally forwards of the given square from the perspective of a pawn of
     * the given colour.
     * @param pawnState
     * @return
     */
    private Set<Optional<Square>> getPawnDiagonals(PieceState pawnState) {
        Set<Optional<Square>> diagonals = new HashSet<>();
        if (pawnState.getColour() == Colour.WHITE) {
            diagonals.add(getNorthEast(pawnState.getSquare()));
            diagonals.add(getNorthWest(pawnState.getSquare()));
        } else {
            diagonals.add(getSouthEast(pawnState.getSquare()));
            diagonals.add(getSouthWest(pawnState.getSquare()));
        }
        return diagonals;
    }

    /**
     * Retrieves the square forwards of the given square from the perspective of a pawn of the given colour.
     * @param pawnState
     * @return
     */
    private Set<Optional<Square>> getPawnForwards(PieceState pawnState) {
        Set<Optional<Square>> forward = new HashSet<>();
        if (pawnState.getColour() == Colour.WHITE) {
            forward.add(getNorth(pawnState.getSquare()));
        } else {
            forward.add(getSouth(pawnState.getSquare()));
        }
        return forward;
    }

    /**
     * Calculates all squares threatened and moveable by a pawn. Threatened squares are squares on an adjacent
     * diagonal in the direction the pawn is moving, while moveable squares are empty forward squares and unblocked
     * forwards diagonal squares.
     * @param pawnState
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForPawn(PieceState pawnState, PiecesState piecesState) {
        generateStructures(piecesState);

        // Pawn can always threaten on a diagonal
        Set<Square> diagonals = CollectionUtil.getPresent(getPawnDiagonals(pawnState));
        threatenedSquares.addAll(diagonals);

        // Pawn can move on a diagonal if there is an enemy (non-king) piece there
        moveableSquares.addAll(filterByAvailability(diagonals, pawnState.getColour(), SquareAvailability.AVAILABLE));

        // Pawn can move forwards if the square is empty
        Set<Square> forward = CollectionUtil.getPresent(getPawnForwards(pawnState));
        moveableSquares.addAll(filterByAvailability(forward, pawnState.getColour(), SquareAvailability.EMPTY));
    }

    /**
     * Calculates all squares in an L shape from the given square.
     * @param knightState
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForKnight(PieceState knightState, PiecesState piecesState) {
        generateStructures(piecesState);

        // Rows and columns of candidate moveable and threatened squares
        Square square = knightState.getSquare();
        int[] rows = {square.getRowNumber()+2, square.getRowNumber()+2, square.getRowNumber()-2, square.getRowNumber()-2,
                square.getRowNumber()+1, square.getRowNumber()-1, square.getRowNumber()+1, square.getRowNumber()-1};
        int[] columns = {square.getLetterNumber()+1, square.getLetterNumber()-1, square.getLetterNumber()+1, square.getLetterNumber()-1,
                square.getLetterNumber()+2, square.getLetterNumber()+2, square.getLetterNumber()-2, square.getLetterNumber()-2};

        // Build up a collection of candidate moveable and threatened squares
        Set<Optional<Square>> candidates = new HashSet<>();
        for (int num = 0; num < rows.length; num++) {
            candidates.add(getSquare(rows[num], columns[num]));
        }

        // Knight can threaten all of these squares
        Set<Square> presentCandidates = CollectionUtil.getPresent(candidates);
        threatenedSquares.addAll(presentCandidates);

        // Knight can only move to those squares that are not blocked
        moveableSquares.addAll(filterByAvailability(
                presentCandidates, knightState.getColour(), SquareAvailability.AVAILABLE, SquareAvailability.EMPTY));
    }

    /**
     * Marks all adjacent squares as moveable and all adjacent squares that are not blocked by a piece of a different
     * colour as threatened.
     * @param kingState
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForKing(PieceState kingState, PiecesState piecesState) {
        generateStructures(piecesState);

        // Build up a collection of candidate adjacent squares
        Square square = kingState.getSquare();
        Set<Optional<Square>> candidates = new HashSet<>();
        for (int row = square.getRowNumber()-1; row <= square.getRowNumber()+1; row++) {
            for (int column = square.getLetterNumber()-1; column <= square.getLetterNumber()+1; column++) {
                candidates.add(getSquare(row, column));
            }
        }
        candidates.remove(Optional.of(square));

        // King can threaten all of those squares
        Set<Square> presentCandidates = CollectionUtil.getPresent(candidates);
        threatenedSquares.addAll(presentCandidates);

        // King can only move to those squares that are not blocked
        moveableSquares.addAll(filterByAvailability(
                presentCandidates, kingState.getColour(), SquareAvailability.AVAILABLE, SquareAvailability.EMPTY));
    }

    /**
     * Traverses all the squares along a given direction from the current square until the direction
     * is blocked. Empty and available squares are marked as moveable while empty, available and blocked
     * squares are all marked as threatened.
     * @param getInDirection retrieves the square in the direction of a supplied square
     * @param pieceState
     */
    private void calculateSquaresAlongDirection(
            Function<Square, Optional<Square>> getInDirection, PieceState pieceState) {

        Optional<Square> candidate = getInDirection.apply(pieceState.getSquare());

        while (candidate.isPresent()) {
            // Piece can threaten empty, available and blocked squares
            threatenedSquares.add(candidate.get());

            SquareAvailability availability =
                    evaluateSquareAvailability(candidate.get(), pieceState.getColour());
            if (availability == SquareAvailability.BLOCKED) {
                break;
            }
            // Piece can move to empty and available squares
            moveableSquares.add(candidate.get());
            if (availability == SquareAvailability.AVAILABLE) {
                break;
            }
            // Can continue traversing if square is empty
            candidate = getInDirection.apply(candidate.get());
        }
    }

    /**
     * Calls calculateSquaresAlongDirection() for each of the supplied direction functions
     * @param pieceState
     * @param getInDirections
     */
    private void calculateSquaresAlongDirections(
            PieceState pieceState, Function<Square, Optional<Square>>... getInDirections) {
        for (Function<Square, Optional<Square>> getInDirection : getInDirections) {
            calculateSquaresAlongDirection(getInDirection, pieceState);
        }
    }

    /**
     * Calculates all the squares reachable on a diagonal from the current square.
     * @param bishopState
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForBishop(PieceState bishopState, PiecesState piecesState) {
        generateStructures(piecesState);
        calculateSquaresAlongDirections(bishopState, sq -> getNorthEast(sq), sq -> getNorthWest(sq),
                sq -> getSouthEast(sq), sq -> getSouthWest(sq));
    }

    /**
     * Calculates all the squares reachable vertically and horizontally from the current square.
     * @param castleState
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForCastle(PieceState castleState, PiecesState piecesState) {
        generateStructures(piecesState);
        calculateSquaresAlongDirections(castleState, sq -> getNorth(sq), sq -> getSouth(sq), sq -> getEast(sq),
                sq -> getWest(sq));
    }

    /**
     * Calculates all the squares reachable vertically, horizontally and on a diagonal from the current square.
     * @param queenState
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForQueen(PieceState queenState, PiecesState piecesState) {
        generateStructures(piecesState);
        calculateSquaresAlongDirections(queenState, sq -> getNorth(sq), sq -> getSouth(sq), sq -> getEast(sq),
                sq -> getWest(sq), sq -> getNorthEast(sq), sq -> getNorthWest(sq), sq -> getSouthEast(sq),
                sq -> getSouthWest(sq));
    }

    /**
     * Populates colour locations map and king locations set based on the provided state of the pieces.
     * @param piecesState
     */
    private void generateStructures(PiecesState piecesState) {
        for (PieceState pieceState : piecesState.getPieceStates()) {
            colourLocations.put(pieceState.getSquare(), pieceState.getColour());
            if (pieceState.getType() == PieceType.KING) {
                kingLocations.add(pieceState.getSquare());
            }
        }
    }

    /**
     * @return previously calculated moveable squares
     */
    public Set<Square> getMoveableSquares() {
        return moveableSquares;
    }

    /**
     * @return previously calculated threatened squares
     */
    public Set<Square> getThreatenedSquares() {
        return threatenedSquares;
    }

    /**
     * Clears previous calculation of threatened and moveable squares
     */
    public void reset() {
        moveableSquares = new HashSet<>();
        threatenedSquares = new HashSet<>();
        colourLocations = new HashMap<>();
        kingLocations = new HashSet<>();
    }
}
