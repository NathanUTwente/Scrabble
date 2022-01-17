package Scrabble.Model.BoardModel;

public class Tile {

    public enum TileType{
        BLANK, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
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
