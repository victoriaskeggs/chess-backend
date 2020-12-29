package model;

public enum Square {
    /**
     * Mapping of squares to row and column numbers
     */
    A1(1, 1), A2(2, 1), A3(3, 1), A4(4, 1), A5(5, 1), A6(6, 1), A7(7, 1), A8(8, 1),
    B1(1, 2), B2(2, 2), B3(3, 2), B4(4, 2), B5(5, 2), B6(6, 2), B7(7, 2), B8(8, 2),
    C1(1, 3), C2(2, 3), C3(3, 3), C4(4, 3), C5(5, 3), C6(6, 3), C7(7, 3), C8(8, 3),
    D1(1, 4), D2(2, 4), D3(3, 4), D4(4, 4), D5(5, 4), D6(6, 4), D7(7, 4), D8(8, 4),
    E1(1, 5), E2(2, 5), E3(3, 5), E4(4, 5), E5(5, 5), E6(6, 5), E7(7, 5), E8(8, 5),
    F1(1, 6), F2(2, 6), F3(3, 6), F4(4, 6), F5(5, 6), F6(6, 6), F7(7, 6), F8(8, 6),
    G1(1, 7), G2(2, 7), G3(3, 7), G4(4, 7), G5(5, 7), G6(6, 7), G7(7, 7), G8(8, 7),
    H1(1, 8), H2(2, 8), H3(3, 8), H4(4, 8), H5(5, 8), H6(6, 8), H7(7, 8), H8(8, 8);

    public static final int NUM_ROWS = 8;
    public static final int NUM_SQUARES_IN_ROW = 8;

    /**
     * Mapping of row and column positions to squares
     */
    private static final Square[][] GRID = {
            {A1, B1, C1, D1, E1, F1, G1, H1},
            {A2, B2, C2, D2, E2, F2, G2, H2},
            {A3, B3, C3, D3, E3, F3, G3, H3},
            {A4, B4, C4, D4, E4, F4, G4, H4},
            {A5, B5, C5, D5, E5, F5, G5, H5},
            {A6, B6, C6, D6, E6, F6, G6, H6},
            {A7, B7, C7, D7, E7, F7, G7, H7},
            {A8, B8, C8, D8, E8, F8, G8, H8}};

    /**
     * Retrieves a square by its row number and letter number
     * @param rowNum between 1 and 8 inclusive
     * @param letterNum where A=1, B=2, etc.
     * @return
     */
    public static Square byPosition(int rowNum, int letterNum) {
        if (rowNum <= 0 || rowNum > NUM_ROWS || letterNum <= 0 || letterNum > NUM_SQUARES_IN_ROW) {
            throw new RuntimeException(String.format("No square at row number %d and letter number %d.", rowNum, letterNum)); // TODO log
        }
        return GRID[rowNum - 1][letterNum - 1];
    }

    private final int rowNumber;
    private final int letterNumber;

    Square(int rowNumber, int letterNumber) {
        this.rowNumber = rowNumber;
        this.letterNumber = letterNumber;
    }

    /**
     * @return the row number of the square, between 1 and 8 inclusive
     */
    public int getRowNumber(){
        return rowNumber;
    }

    /**
     * @return the letter number of the square, where A=1, B=2, etc.
     */
    public int getLetterNumber() {
        return letterNumber;
    }
}
