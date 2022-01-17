package Scrabble.Model;

public class Tile {

    public enum TileType{
        BLANK, AA, BB, CC, DD, EE, FF, GG, HH, II, JJ, KK, LL, MM, NN, OO, PP, QQ, RR, SS, TT, UU, VV, WW, XX, YY, ZZ
    }
    public enum SpecialType{
        DOUBLE_WORD, TRIPLE_WORD, DOUBLE_LETTER, TRIPLE_LETTER, CENTRE
    }

    private TileType tileType;
    private int points;
    private SpecialType specialType;



    public Tile(TileType tileType, int points) {
        this.tileType = tileType;
        this.points = points;
        this.specialType = null;
    }

    public Tile(TileType tileType, int points, SpecialType specialType) {
        this.tileType = tileType;
        this.points = points;
        this.specialType = specialType;
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getPoints() {
        return points;
    }

    public SpecialType getSpecialType() {
        return specialType;
    }
}
