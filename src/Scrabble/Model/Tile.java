package Scrabble.Model;

public class Tile {

    public enum TileType{
        BLANK, AA, BB, CC, DD, EE, FF, GG, HH, II, JJ, KK, LL, MM, NN, OO, PP, QQ, RR, SS, TT, UU, VV, WW, XX, YY, ZZ
    }

    private TileType tileType;
    private int points;



    public Tile(TileType tileType, int points) {
        this.tileType = tileType;
        this.points = points;
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getPoints() {
        return points;
    }

}
