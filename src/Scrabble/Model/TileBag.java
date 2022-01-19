package Scrabble.Model;


import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.PlayerModels.Player;

import java.util.ArrayList;
import java.util.Random;

public class TileBag {
    public ArrayList<Tile> tileList;
    final private ArrayList<Tile.TileType> letters;

    public static void main(String[] args) {
        new TileBag();
    }

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
        
        letters = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            letters.add(Tile.TileType.E);
        }

        for (int i = 0; i < 9; i++) {
            letters.add(Tile.TileType.A);
            letters.add(Tile.TileType.I);
        }

        for (int i = 0; i < 8; i++) {
            letters.add(Tile.TileType.O);
        }

        for (int i = 0; i < 6; i++) {
            letters.add(Tile.TileType.N);
            letters.add(Tile.TileType.R);
            letters.add(Tile.TileType.T);
        }

        for (int i = 0; i < 4; i++) {
            letters.add(Tile.TileType.L);
            letters.add(Tile.TileType.S);
            letters.add(Tile.TileType.U);
            letters.add(Tile.TileType.D);
        }

        for (int i = 0; i < 3; i++) {
            letters.add(Tile.TileType.G);
        }

        for (int i = 0; i < 2; i++) {
            letters.add(Tile.TileType.B);
            letters.add(Tile.TileType.C);
            letters.add(Tile.TileType.M);
            letters.add(Tile.TileType.P);
            letters.add(Tile.TileType.F);
            letters.add(Tile.TileType.H);
            letters.add(Tile.TileType.V);
            letters.add(Tile.TileType.W);
            letters.add(Tile.TileType.Y);
            letters.add(Tile.TileType.BLANK);
        }

        letters.add(Tile.TileType.K);
        letters.add(Tile.TileType.J);
        letters.add(Tile.TileType.X);
        letters.add(Tile.TileType.Q);
        letters.add(Tile.TileType.Z);
    }

    public int tilesLeftInBag(){
        return letters.size();
    }

    public Tile.TileType getTileOutOfBag(){
        if (tilesLeftInBag() > 0) {
            int max = letters.size() - 1;
            Random random = new Random();
            int randomTileIndex = random.nextInt(max + 1);
            return letters.remove(randomTileIndex);
        } else {
            return null;
        }
    }

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
            case "BLANK":
                return Tile.TileType.BLANK;
            default:

            }
            return null;
    }

    public static int GetPointOfTile(String letter){
        TileBag tileBag = new TileBag();
        for (Tile tile : tileBag.tileList){
            if (tile.getTileLetter().equals(letter)){
                return tile.getPoints();
            }
        }
        return 0;
    }

    public ArrayList<Tile.TileType> givePlayerTile(Player player){
        ArrayList<Tile.TileType> newTile = new ArrayList<>();
        int needed = player.EmptySpotsInDeck();
        for (int i = 0; i < needed; i++) {
            newTile.add(getTileOutOfBag());
            return newTile;
        }
        return null;
    }


}
