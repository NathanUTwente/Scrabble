package Scrabble.Model;

public class Square {
    private Tile tile;
    private int[] location;


    public Square(int[] location) {
        this.location = location;
        this.tile = new Tile(Tile.TileType.BLANK, 0);
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
}
