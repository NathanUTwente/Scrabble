package Scrabble.Model.BoardModel;

public class Tile {

    public enum TileType{
        BLANK, AA, B, CC, DD, EE, FF, GG, HH, II, JJ, KK, LL, MM, NN, OO, PP, QQ, RR, SS, TT, UU, VV, WW, XX, YY, ZZ
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
    
    public String getTileLetter(){
        if (this.tileType == TileType.BLANK){
            return " ";
        } else return String.valueOf(this.tileType);
    }

}
