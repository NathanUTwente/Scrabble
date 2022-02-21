package Scrabble.Model.BoardModel;

import Scrabble.View.TextBoardRepresentation;

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

    /**
     * returns the tile type
     * @return tileType
     */
    public TileType getTileType() {
        return tileType;
    }

    /**
     * returns the points of the player
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * returns the tileType of the string inputed or _ if blank is given
     * @return tile type or _
     */
    public String getTileLetter(){
        if (this.tileType == TileType.BLANK){
            return "_";
        } else return String.valueOf(this.tileType);
    }

}
