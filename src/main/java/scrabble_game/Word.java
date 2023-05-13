package scrabble_game;

import java.util.Arrays;

public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        // casting from object to word type.
        Word word = (Word)o;
        return getRow() == word.getRow() && getCol() == word.getCol() && isVertical() == word.isVertical() && Arrays.equals(getTiles(), word.getTiles());
    }

}
