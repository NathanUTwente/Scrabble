package Scrabble.Model;


import Scrabble.Model.BoardModel.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileBag {
    public ArrayList<Tile.TileType> tileList;
    final private ArrayList<Tile.TileType> letters;

    public TileBag(){
        tileList = new ArrayList<>();
        
        // used for scoring
        tileList.add(1, Tile.TileType.A);
        tileList.add(3, Tile.TileType.B);
        tileList.add(3, Tile.TileType.C);
        tileList.add(2, Tile.TileType.D);
        tileList.add(1, Tile.TileType.E);
        tileList.add(4, Tile.TileType.F);
        tileList.add(2, Tile.TileType.G);
        tileList.add(4, Tile.TileType.H);
        tileList.add(1, Tile.TileType.I);
        tileList.add(8, Tile.TileType.J);
        tileList.add(5, Tile.TileType.K);
        tileList.add(1, Tile.TileType.L);
        tileList.add(3, Tile.TileType.M);
        tileList.add(1, Tile.TileType.N);
        tileList.add(1, Tile.TileType.O);
        tileList.add(3, Tile.TileType.P);
        tileList.add(10, Tile.TileType.Q);
        tileList.add(1, Tile.TileType.R);
        tileList.add(1, Tile.TileType.S);
        tileList.add(1, Tile.TileType.T);
        tileList.add(1, Tile.TileType.U);
        tileList.add(4, Tile.TileType.V);
        tileList.add(4, Tile.TileType.W);
        tileList.add(8, Tile.TileType.X);
        tileList.add(4, Tile.TileType.Y);
        tileList.add(10, Tile.TileType.Z);
        tileList.add(0, Tile.TileType.BLANK);
        
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
        int max = letters.size() - 1;
        Random random = new Random();
        int randomTileIndex = random.nextInt(max+1);
        return letters.remove(randomTileIndex);
    }

    public int getScoreOfLetter(char letter){
        int score = 0;

        }
    }
}
