package Scrabble.Model.BoardModel;

import java.util.Arrays;

public class Square {
    private Tile tile = null;
    private int[] location;
    private SpecialType specialType;


    public enum SpecialType{
        DOUBLE_WORD, TRIPLE_WORD, DOUBLE_LETTER, TRIPLE_LETTER, CENTRE
    }


    public Square(int[] location) {
        this.location = location;
        this.specialType = null;
    }

    public Square(int[] location, SpecialType specialType) {
        this.location = location;
        this.specialType = specialType;
    }

    public int[] getLocation() {
        return location;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public SpecialType getSpecialType() {
        return specialType;
    }

    @Override
    public String toString() {
        return "Square{" +
                "tile=" + tile +
                ", location=" + Arrays.toString(location) +
                ", specialType=" + specialType +
                '}';
    }
}
