package Scrabble.Model;
import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.PlayerModels.Player;
import java.util.ArrayList;
import java.util.Random;

public class TileBag {
    public ArrayList<Tile> tileList;
    final private ArrayList<Tile> tileBag;


    public TileBag(){
        tileList = new ArrayList<>();
        
        // used for scoring
        tileList.add(new Tile(Tile.TileType.A, 1));
        tileList.add(new Tile(Tile.TileType.B, 3));
        tileList.add(new Tile(Tile.TileType.C, 3));
        tileList.add(new Tile(Tile.TileType.D, 2));
        tileList.add(new Tile(Tile.TileType.E, 1));
        tileList.add(new Tile(Tile.TileType.F, 4));
        tileList.add(new Tile(Tile.TileType.G, 2));
        tileList.add(new Tile(Tile.TileType.H, 4));
        tileList.add(new Tile(Tile.TileType.I, 1));
        tileList.add(new Tile(Tile.TileType.J, 8));
        tileList.add(new Tile(Tile.TileType.K, 5));
        tileList.add(new Tile(Tile.TileType.L, 1));
        tileList.add(new Tile(Tile.TileType.M, 3));
        tileList.add(new Tile(Tile.TileType.N, 1));
        tileList.add(new Tile(Tile.TileType.O, 1));
        tileList.add(new Tile(Tile.TileType.P, 3));
        tileList.add(new Tile(Tile.TileType.Q, 10));
        tileList.add(new Tile(Tile.TileType.R, 1));
        tileList.add(new Tile(Tile.TileType.S, 1));
        tileList.add(new Tile(Tile.TileType.T, 1));
        tileList.add(new Tile(Tile.TileType.U, 1));
        tileList.add(new Tile(Tile.TileType.V, 4));
        tileList.add(new Tile(Tile.TileType.W, 4));
        tileList.add(new Tile(Tile.TileType.X, 8));
        tileList.add(new Tile(Tile.TileType.Y, 4));
        tileList.add(new Tile(Tile.TileType.Z, 10));
        tileList.add(new Tile(Tile.TileType.BLANK, 0));
        
        tileBag = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            tileBag.add(new Tile(Tile.TileType.E, 1));
        }

        for (int i = 0; i < 9; i++) {
            tileBag.add(new Tile(Tile.TileType.A, 1));
            tileBag.add(new Tile(Tile.TileType.I, 1));
        }

        for (int i = 0; i < 8; i++) {
            tileBag.add(new Tile(Tile.TileType.O, 1));
        }

        for (int i = 0; i < 6; i++) {
            tileBag.add(new Tile(Tile.TileType.N, 1));
            tileBag.add(new Tile(Tile.TileType.R, 1));
            tileBag.add(new Tile(Tile.TileType.T, 1));
        }

        for (int i = 0; i < 4; i++) {
            tileBag.add(new Tile(Tile.TileType.L, 1));
            tileBag.add(new Tile(Tile.TileType.S, 1));
            tileBag.add(new Tile(Tile.TileType.U, 1));
            tileBag.add(new Tile(Tile.TileType.D, 1));
        }

        for (int i = 0; i < 3; i++) {
            tileBag.add(new Tile(Tile.TileType.G, 2));
        }

        for (int i = 0; i < 2; i++) {
            tileBag.add(new Tile(Tile.TileType.B, 3));
            tileBag.add(new Tile(Tile.TileType.C, 3));
            tileBag.add(new Tile(Tile.TileType.M, 3));
            tileBag.add(new Tile(Tile.TileType.P, 3));
            tileBag.add(new Tile(Tile.TileType.F, 4));
            tileBag.add(new Tile(Tile.TileType.H, 4));
            tileBag.add(new Tile(Tile.TileType.V, 4));
            tileBag.add(new Tile(Tile.TileType.W, 4));
            tileBag.add(new Tile(Tile.TileType.Y, 4));
            tileBag.add(new Tile(Tile.TileType.BLANK, 0));
            tileBag.add(new Tile(Tile.TileType.K, 5));
            tileBag.add(new Tile(Tile.TileType.J, 8));
        }

        tileBag.add(new Tile(Tile.TileType.X, 8));
        tileBag.add(new Tile(Tile.TileType.Q, 10));
        tileBag.add(new Tile(Tile.TileType.Z, 10));
    }

    /**
     * returns the number of tiles left in the bag
     * @return tilebag of player
     */
    public int tilesLeftInBag(){
        return tileBag.size();
    }

    /**
     * @ensures tiles left is bigger than 0
     * when player uses tiles this method removes the played tiles from the tilebag
     * @return tilebag without the tiles
     */
    public Tile getTileOutOfBag(){
        if (tilesLeftInBag() > 0) {
            int max = tileBag.size() - 1;
            Random random = new Random();
            int randomTileIndex = random.nextInt(max + 1);
            return tileBag.remove(randomTileIndex);
        } else {
            return null;
        }
    }

    /**
     * turns the string given when move is played into a tile
     * @param letter
     * @return letter in tile
     */
    public static Tile.TileType stringToTile(String letter){
        switch (letter){
            case "A":
            return Tile.TileType.A;
            case "B":
                return Tile.TileType.B;
            case "C":
                return Tile.TileType.C;
            case "D":
                return Tile.TileType.D;
            case "E":
                return Tile.TileType.E;
            case "F":
                return Tile.TileType.F;
            case "G":
                return Tile.TileType.G;
            case "H":
                return Tile.TileType.H;
            case "I":
                return Tile.TileType.I;
            case "J":
                return Tile.TileType.J;
            case "K":
                return Tile.TileType.K;
            case "L":
                return Tile.TileType.L;
            case "M":
                return Tile.TileType.M;
            case "N":
                return Tile.TileType.N;
            case "O":
                return Tile.TileType.O;
            case "P":
                return Tile.TileType.P;
            case "Q":
                return Tile.TileType.Q;
            case "R":
                return Tile.TileType.R;
            case "S":
                return Tile.TileType.S;
            case "T":
                return Tile.TileType.T;
            case "U":
                return Tile.TileType.U;
            case "V":
                return Tile.TileType.V;
            case "W":
                return Tile.TileType.W;
            case "X":
                return Tile.TileType.X;
            case "Y":
                return Tile.TileType.Y;
            case "Z":
                return Tile.TileType.Z;
            case "_":
                return Tile.TileType.BLANK;
            default:

            }
            return null;
    }

    /**
     * returns the points of the tile
     * @param letter
     * @return points of letter
     */
    public static int GetPointOfTile(String letter){
        TileBag tileBag = new TileBag();
        for (Tile tile : tileBag.tileList){
            if (tile.getTileLetter().equals(letter)){
                return tile.getPoints();
            }
        }
        return 0;
    }

    /**
     * when player makes a move this method checks how many tiles are used and restocks them with new ones
     * @param player
     * @return an arraylist of tiles
     */
    public ArrayList<Tile> getTilesForPlayer(Player player){
        ArrayList<Tile> newTile = new ArrayList<>();
        int needed = player.EmptySpotsInDeck();
        for (int i = 0; i < needed; i++) {
            Tile tile = getTileOutOfBag();
            if (tile != null) {
                newTile.add(tile);
            } else {
                break;
            }
        }
        return newTile;
    }


}
