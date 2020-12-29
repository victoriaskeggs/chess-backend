package model.util;

import model.Colour;
import model.Square;
import model.piecestate.PiecesState;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class for calculating which squares a piece can move to.
 */
public class MovesUtil {

    /**
     * Whether a piece is available for a piece to move into.
     */
    private enum SquareAvailability {
        EMPTY, // no piece in the square
        AVAILABLE, // piece in the square that can be taken
        BLOCKED // piece in the square that cannot be taken
    }

    /**
     * Checks if a square with a given row and column number is inside the chess board.
     * @param row
     * @param column
     * @return true if square is inside the board
     */
    private static boolean isInsideGrid(int row, int column) {
        return row > 0 && row <= Square.NUM_ROWS && column > 0 && column <= Square.NUM_SQUARES_IN_ROW;
    }

    /**
     * Retrieves a square with the given row and column if it is inside the grid.
     * @param row
     * @param column
     * @return an optional containing the square if it exists, an empty optional otherwise
     */
    private static Optional<Square> getSquare(int row, int column) {
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
    private static Optional<Square> getNorth(Square square) {
        return getSquare(square.getRowNumber()+1, square.getLetterNumber());
    }

    /**
     * Retrieves the square directly south of the current square, where row 1 is south and row 8 is noth.
     * @param square
     * @return an optional containing the southern square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getSouth(Square square) {
        return getSquare(square.getRowNumber()-1, square.getLetterNumber());
    }

    /**
     * Retrieves the square directly east of the current square, where column A is west and column H is east.
     * @param square
     * @return an optional containing the eastern square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getEast(Square square) {
        return getSquare(square.getRowNumber(), square.getLetterNumber()+1);
    }

    /**
     * Retrieves the square directly west of the current square, where column A is west and column H is east.
     * @param square
     * @return an optional containing the western square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getWest(Square square) {
        return getSquare(square.getRowNumber(), square.getLetterNumber()-1);
    }

    /**
     * Retrieves the square directly northeast of the current square, where H8 is the most
     * northeastern square
     * @param square
     * @return an optional containing the northeastern square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getNorthEast(Square square) {
        return getSquare(square.getRowNumber()+1, square.getLetterNumber()+1);
    }

    /**
     * Retrieves the square directly northwest of the current square, where A8 is the most
     * northwestern square
     * @param square
     * @return an optional containing the northwestern square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getNorthWest(Square square) {
        return getSquare(square.getRowNumber()+1, square.getLetterNumber()-1);
    }

    /**
     * Retrieves the square directly southeast of the current square, where H1 is the most
     * southeastern square
     * @param square
     * @return an optional containing the southeastern square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getSouthEast(Square square) {
        return getSquare(square.getRowNumber()-1, square.getLetterNumber()+1);
    }

    /**
     * Retrieves the square directly southwest of the current square, where A1 is the most
     * southwestern square
     * @param square
     * @return an optional containing the southwestern square if it exists, otherwise an empty optional
     */
    private static Optional<Square> getSouthWest(Square square) {
        return getSquare(square.getRowNumber()-1, square.getLetterNumber()-1);
    }

    /**
     * Checks if a square is available for a piece to move into
     * @param square
     * @param colour
     * @param piecesState
     * @return availability of the square
     */
    private static SquareAvailability evaluateSquareAvailability(
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
     * @param colour
     * @param piecesState
     * @param allowed allowed availabilities
     * @return filtered set
     */
    private static Set<Square> filterByAvailability(
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
    private static Set<Optional<Square>> getPawnDiagonals(Square square, Colour colour) {
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
    private static Set<Optional<Square>> getPawnForwards(Square square, Colour colour) {
        Set<Optional<Square>> forward = new HashSet<>();
        if (colour == Colour.WHITE) {
            forward.add(getNorth(square));
        } else {
            forward.add(getSouth(square));
        }
        return forward;
    }

    /**
     * Retrieves all squares reachable by a pawn, ie:
     * - empty squares in the direction the pawn is moving
     * - squares occupied (not by a king) on an adjacent diagonal in the direction the pawn is moving
     * @param square
     * @param colour
     * @param piecesState
     * @return moveable squares
     */
    public static Set<Square> getMoveableSquaresForPawn(
            Square square, Colour colour, PiecesState piecesState) {

        // Candidates for moving on a diagonal
        Set<Optional<Square>> diagonals = getPawnDiagonals(square, colour);

        // Pawn can only move on a diagonal if there is an enemy piece there
        Set<SquareAvailability> availableAsSet = new HashSet<>();
        availableAsSet.add(SquareAvailability.AVAILABLE);
        Set<Square> moveable = filterByAvailability(diagonals, Colour.WHITE, piecesState, availableAsSet);

        // Candidate for moving forward
        Set<Optional<Square>> forward = getPawnForwards(square, colour);

        // Pawn can only move forward if the square is empty
        Set<SquareAvailability> emptyAsSet = new HashSet<>();
        emptyAsSet.add(SquareAvailability.EMPTY);
        moveable.addAll(filterByAvailability(forward, Colour.WHITE, piecesState, emptyAsSet));

        return moveable;
    }

    /**
     * Retrieves all squares in an L shape from the given square.
     * @param square
     * @param colour
     * @param piecesState
     * @return squares an L shape away
     */
    public static Set<Square> getMoveableSquaresForKnight(
            Square square, Colour colour, PiecesState piecesState) {

        // Rows and columns of candidate squares
        int[] rows = {square.getRowNumber()+2, square.getRowNumber()+2, square.getRowNumber()-2, square.getRowNumber()-2,
                square.getRowNumber()+1, square.getRowNumber()-1, square.getRowNumber()+1, square.getRowNumber()-1};
        int[] columns = {square.getLetterNumber()+1, square.getLetterNumber()-1, square.getLetterNumber()+1, square.getLetterNumber()-1,
                square.getLetterNumber()+2, square.getLetterNumber()-2, square.getLetterNumber()+2, square.getLetterNumber()-2};

        // Build up a collection of candidate squares
        Set<Optional<Square>> candidates = new HashSet<>();
        for (int num = 0; num < rows.length; num++) {
            candidates.add(getSquare(rows[num], columns[num]));
        }

        // Only keep the squares that exist and are not blocked
        Set<SquareAvailability> allowed = new HashSet<>();
        allowed.add(SquareAvailability.AVAILABLE);
        allowed.add(SquareAvailability.EMPTY);
        return filterByAvailability(candidates, colour, piecesState, allowed);
    }

    /**
     * Retrieves all adjacent squares that are not blocked by a piece of a different colour.
     * @param square
     * @param colour
     * @param piecesState
     * @return adjacent squares
     */
    public static Set<Square> getMoveableSquaresForKing(
            Square square, Colour colour, PiecesState piecesState) {

        // Build up a collection of candidate adjacent squares
        Set<Optional<Square>> candidates = new HashSet<>();
        for (int row = square.getRowNumber()-1; row <= square.getRowNumber()+1; row++) {
            for (int column = square.getLetterNumber()-1; column <= square.getLetterNumber()+1; column++) {
                candidates.add(getSquare(row, column));
            }
        }
        candidates.remove(Optional.of(square));

        // Only keep the squares that exist and are not blocked
        Set<SquareAvailability> allowed = new HashSet<>();
        allowed.add(SquareAvailability.AVAILABLE);
        allowed.add(SquareAvailability.EMPTY);
        return filterByAvailability(candidates, colour, piecesState, allowed);
    }

    /**
     * Retrieves all the squares along a given direction from the current square until the direction
     * is blocked.
     * @param square
     * @param getInDirection retrieves the square in the direction of a supplied square
     * @param colour
     * @param piecesState
     * @return unblocked squares
     */
    private static Set<Square> getUnblockedSquaresAlongDirection(
            Square square, Function<Square, Optional<Square>> getInDirection, Colour colour,
            PiecesState piecesState) {

        Set<Square> notBlocked = new HashSet<>();
        Optional<Square> candidate = getInDirection.apply(square);

        while (candidate.isPresent()) {
            SquareAvailability availability =
                    evaluateSquareAvailability(candidate.get(), colour, piecesState);
            if (availability == SquareAvailability.BLOCKED) {
                break;
            }
            notBlocked.add(candidate.get());
            if (availability == SquareAvailability.AVAILABLE) {
                break;
            }
            candidate = getInDirection.apply(candidate.get());
        }
        return notBlocked;
    }

    /**
     * Retrieves all the squares reachable on a diagonal from the current square.
     * @param square
     * @param colour
     * @param piecesState
     * @return diagonal squares
     */
    public static Set<Square> getMoveableSquaresForBishop(
            Square square, Colour colour, PiecesState piecesState) {
        Set<Square> diagonals = new HashSet<>();
        diagonals.addAll(getUnblockedSquaresAlongDirection(square, sq -> getNorthEast(sq), colour, piecesState));
        diagonals.addAll(getUnblockedSquaresAlongDirection(square, sq -> getNorthWest(sq), colour, piecesState));
        diagonals.addAll(getUnblockedSquaresAlongDirection(square, sq -> getSouthEast(sq), colour, piecesState));
        diagonals.addAll(getUnblockedSquaresAlongDirection(square, sq -> getSouthWest(sq), colour, piecesState));
        return diagonals;
    }

    /**
     * Retrieves all the squares reachable vertically and horizontally from the current square.
     * @param square
     * @param colour
     * @param piecesState
     * @return vertical and horizontal squares
     */
    public static Set<Square> getMoveableSquaresForCastle(
            Square square, Colour colour, PiecesState piecesState) {
        Set<Square> straights = new HashSet<>();
        straights.addAll(getUnblockedSquaresAlongDirection(square, sq -> getNorth(sq), colour, piecesState));
        straights.addAll(getUnblockedSquaresAlongDirection(square, sq -> getSouth(sq), colour, piecesState));
        straights.addAll(getUnblockedSquaresAlongDirection(square, sq -> getEast(sq), colour, piecesState));
        straights.addAll(getUnblockedSquaresAlongDirection(square, sq -> getWest(sq), colour, piecesState));
        return straights;
    }

    /**
     * Retrieves all the squares reachable vertically, horizontally and on a diagonal from the current square.
     * @param square
     * @param colour
     * @param piecesState
     * @return vertical, horizontal and diagonal squares
     */
    public static Set<Square> getMoveableSquaresForQueen(
            Square square, Colour colour, PiecesState piecesState) {
        Set<Square> squares = new HashSet<>();
        squares.addAll(getMoveableSquaresForBishop(square, colour, piecesState));
        squares.addAll(getMoveableSquaresForCastle(square, colour, piecesState));
        return squares;
    }
}
