package model.util;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;
import util.CollectionUtil;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class for calculating which squares a piece can move to.
 */
public class MovesCalculator {

    private Set<Square> moveableSquares;
    private Set<Square> threatenedSquares;

    /**
     * Whether a piece is available for a piece to move into.
     */
    private enum SquareAvailability {
        EMPTY, // no piece in the square
        AVAILABLE, // piece in the square that can be taken
        BLOCKED // piece in the square that cannot be taken
    }

    public MovesCalculator() {
        moveableSquares = new HashSet<>();
        threatenedSquares = new HashSet<>();
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
     * Checks if a square is available for a piece to move into
     * @param square
     * @param colour
     * @param piecesState
     * @return availability of the square
     */
    private SquareAvailability evaluateSquareAvailability(
            Square square, Colour colour, PiecesState piecesState) {
        // If the square is not occupied, it is empty
        if (!piecesState.getColourLocations().containsKey(square)) {
            return SquareAvailability.EMPTY;
        }
        // If the square is occupied by a king, it is blocked
        if (piecesState.getKingLocations().contains(square)) {
            return SquareAvailability.BLOCKED;
        }
        // If the square is occupied by a piece (not a king) of the same colour, it is also blocked
        if (piecesState.getColourLocations().get(square) == colour) {
            return SquareAvailability.BLOCKED;
        }
        // Otherwise the square is occupied by a piece of a different colour and therefore available
        return SquareAvailability.AVAILABLE;
    }

    /**
     * Filters the candidate squares to those that have an allowed availability.
     * @param candidates to filter
     * @param colour of piece hoping to move to the square
     * @param piecesState
     * @param allowed allowed availabilities
     * @return filtered set
     */
    private Set<Square> filterByAvailability(
            Set<Optional<Square>> candidates, Colour colour, PiecesState piecesState,
            Set<SquareAvailability> allowed) {

        Set<Square> filtered = new HashSet<>();
        for (Optional<Square> candidate : candidates) {
            if (candidate.isPresent()) {
                SquareAvailability availability = evaluateSquareAvailability(candidate.get(), colour, piecesState);
                if (allowed.contains(availability)) {
                    filtered.add(candidate.get());
                }
            }
        }
        return filtered;
    }

    /**
     * Retrieves the squares diagonally forwards of the given square from the perspective of a pawn of
     * the given colour.
     * @param square
     * @param colour
     * @return
     */
    private Set<Optional<Square>> getPawnDiagonals(Square square, Colour colour) {
        Set<Optional<Square>> diagonals = new HashSet<>();
        if (colour == Colour.WHITE) {
            diagonals.add(getNorthEast(square));
            diagonals.add(getNorthWest(square));
        } else {
            diagonals.add(getSouthEast(square));
            diagonals.add(getSouthWest(square));
        }
        return diagonals;
    }

    /**
     * Retrieves the square forwards of the given square from the perspective of a pawn of the given colour.
     * @param square
     * @param colour
     * @return
     */
    private Set<Optional<Square>> getPawnForwards(Square square, Colour colour) {
        Set<Optional<Square>> forward = new HashSet<>();
        if (colour == Colour.WHITE) {
            forward.add(getNorth(square));
        } else {
            forward.add(getSouth(square));
        }
        return forward;
    }

    /**
     * Calculates all squares threatened and moveable by a pawn. Threatened squares are squares on an adjacent
     * diagonal in the direction the pawn is moving, while moveable squares are empty forward squares and unblocked
     * forwards diagonal squares.
     * @param square
     * @param colour
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForPawn(
            Square square, Colour colour, PiecesState piecesState) {

        // Candidates for moving on a diagonal
        Set<Optional<Square>> diagonals = getPawnDiagonals(square, colour);

        // Pawn can always threaten on a diagonal
        threatenedSquares.addAll(CollectionUtil.getPresent(diagonals));

        // Pawn can only move on a diagonal if there is an enemy (non-king) piece there
        Set<SquareAvailability> availableAsSet = new HashSet<>();
        availableAsSet.add(SquareAvailability.AVAILABLE);
        moveableSquares.addAll(filterByAvailability(diagonals, Colour.WHITE, piecesState, availableAsSet));

        // Candidate for moving forward
        Set<Optional<Square>> forward = getPawnForwards(square, colour);

        // Pawn can only move forward if the square is empty
        Set<SquareAvailability> emptyAsSet = new HashSet<>();
        emptyAsSet.add(SquareAvailability.EMPTY);
        moveableSquares.addAll(filterByAvailability(forward, Colour.WHITE, piecesState, emptyAsSet));
    }

    /**
     * Calculates all squares in an L shape from the given square.
     * @param square
     * @param colour
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForKnight(
            Square square, Colour colour, PiecesState piecesState) {

        // Rows and columns of candidate moveable and threatened squares
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
        threatenedSquares.addAll(CollectionUtil.getPresent(candidates));

        // Knight can only move to those squares that are not blocked
        Set<SquareAvailability> allowed = new HashSet<>();
        allowed.add(SquareAvailability.AVAILABLE);
        allowed.add(SquareAvailability.EMPTY);
        moveableSquares.addAll(filterByAvailability(candidates, colour, piecesState, allowed));
    }

    /**
     * Marks all adjacent squares as moveable and all adjacent squares that are not blocked by a piece of a different
     * colour as threatened.
     * @param square
     * @param colour
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForKing(
            Square square, Colour colour, PiecesState piecesState) {

        // Build up a collection of candidate adjacent squares
        Set<Optional<Square>> candidates = new HashSet<>();
        for (int row = square.getRowNumber()-1; row <= square.getRowNumber()+1; row++) {
            for (int column = square.getLetterNumber()-1; column <= square.getLetterNumber()+1; column++) {
                candidates.add(getSquare(row, column));
            }
        }
        candidates.remove(Optional.of(square));

        // King can threaten all of those squares
        threatenedSquares.addAll(CollectionUtil.getPresent(candidates));

        // King can only move to those squares that are not blocked
        Set<SquareAvailability> allowed = new HashSet<>();
        allowed.add(SquareAvailability.AVAILABLE);
        allowed.add(SquareAvailability.EMPTY);
        moveableSquares.addAll(filterByAvailability(candidates, colour, piecesState, allowed));
    }

    /**
     * Traverses all the squares along a given direction from the current square until the direction
     * is blocked. Empty and available squares are marked as moveable while empty, available and blocked
     * squares are all marked as threatened.
     * @param square
     * @param getInDirection retrieves the square in the direction of a supplied square
     * @param colour
     * @param piecesState
     */
    private void calculateThreatenedAndMoveableSquaresAlongDirection(
            Square square, Function<Square, Optional<Square>> getInDirection, Colour colour,
            PiecesState piecesState) {

        Optional<Square> candidate = getInDirection.apply(square);

        while (candidate.isPresent()) {
            // Piece can threaten empty, available and blocked squares
            threatenedSquares.add(candidate.get());

            SquareAvailability availability =
                    evaluateSquareAvailability(candidate.get(), colour, piecesState);
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
     * Calculates all the squares reachable on a diagonal from the current square.
     * @param square
     * @param colour
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForBishop(
            Square square, Colour colour, PiecesState piecesState) {
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getNorthEast(sq), colour, piecesState);
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getNorthWest(sq), colour, piecesState);
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getSouthEast(sq), colour, piecesState);
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getSouthWest(sq), colour, piecesState);
    }

    /**
     * Calculates all the squares reachable vertically and horizontally from the current square.
     * @param square
     * @param colour
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForCastle(
            Square square, Colour colour, PiecesState piecesState) {
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getNorth(sq), colour, piecesState);
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getSouth(sq), colour, piecesState);
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getEast(sq), colour, piecesState);
        calculateThreatenedAndMoveableSquaresAlongDirection(square, sq -> getWest(sq), colour, piecesState);
    }

    /**
     * Calculates all the squares reachable vertically, horizontally and on a diagonal from the current square.
     * @param square
     * @param colour
     * @param piecesState
     */
    public void calculateMoveableAndThreatenedSquaresForQueen(
            Square square, Colour colour, PiecesState piecesState) {
        calculateMoveableAndThreatenedSquaresForBishop(square, colour, piecesState);
        calculateMoveableAndThreatenedSquaresForCastle(square, colour, piecesState);
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
    public void resetThreatenedAndMoveableSquares() {
        moveableSquares = new HashSet<>();
        threatenedSquares = new HashSet<>();
    }
}
