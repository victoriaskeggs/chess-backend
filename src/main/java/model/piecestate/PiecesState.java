package model.piecestate;

import model.Colour;
import model.Square;

import java.util.Map;
import java.util.Set;

public class PiecesState {

    private final Map<Square, Colour> colourLocations;
    private final Set<Square> kingLocations;

    /**
     * Constructs a new event indicating the current state of a collection of pieces
     * @param colourLocations maps squares to colour of pieces on the squares, for all the pieces in the collection
     * @param kingLocations locations of kings in the collection of pieces
     */
    public PiecesState(Map<Square, Colour> colourLocations, Set<Square> kingLocations) {
        this.colourLocations = colourLocations;
        this.kingLocations = kingLocations;
    }

    /**
     * @return the locations of the black and white pieces on the board
     */
    public Map<Square, Colour> getColourLocations() {
        return colourLocations;
    }

    /**
     * @return the locations of the kings
     */
    public Set<Square> getKingLocations() {
        return kingLocations;
    }
}
