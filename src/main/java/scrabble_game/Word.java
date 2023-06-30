package scrabble_game;

import java.util.Arrays;

public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;

    /**
     * The Word function returns the word as a string.
     * <p>
     *
     * @param tiles Store the tiles that make up the word
     * @param  row Store the row number of the word
     * @param col Set the column of the word
     * @param vertical Determine whether the word is vertical or horizontal
     */
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    /**
     * The getTiles function returns the tiles array.
     * <p>
     *
     *
     * @return An array of tile objects
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * The getRow function returns the row of the current position.
     * <p>
     *
     *
     * @return The row of the cell
     *
     */
    public int getRow() {
        return row;
    }

    /**
     * The getCol function returns the column of the current position.
     * <p>
     *
     *
     * @return The column of the cell
     *
     */
    public int getCol() {
        return col;
    }

    /**
     * The isVertical function returns a boolean value that indicates whether the
     * current instance of the class is vertical or not.
     * <p>
     *
     *
     * @return A boolean value that is true if the line segment is vertical
     *
     */
    public boolean isVertical() {
        return vertical;
    }
    /**
     * The equals function checks if the object passed in is equal to the current word.
     * <p>
     *
     * @param o Object  Compare the object to another object
     *
     * @return True if the two words are equal
     *
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        // casting from object to word type.
        Word word = (Word)o;
        return getRow() == word.getRow() && getCol() == word.getCol() && isVertical() == word.isVertical() && Arrays.equals(getTiles(), word.getTiles());
    }

}
