package Scrabble.Model.BoardModel;

public class Square {
    private Tile tile;
    private int[] location;
    private SpecialType specialType;


    public enum SpecialType{
        DOUBLE_WORD, TRIPLE_WORD, DOUBLE_LETTER, TRIPLE_LETTER, CENTRE
    }


    public Square(int[] location) {
        this.location = location;
        this.tile = new Tile(Tile.TileType.BLANK, 0);
        this.specialType = null;
    }

    public Square(int[] location, SpecialType specialType) {
        this.location = location;
        this.tile = new Tile(Tile.TileType.BLANK, 0);
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
}
