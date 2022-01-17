package Scrabble.Model.PlayerModels;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
import Scrabble.View.TextBoardRepresentation;

public abstract class Player {
    private String name;
    private Tile[] tileDeck;
    private int points;


    /**
     * Creates a new player with a given name, an empty tile deck and 0 points
     * @param name name of player
     */
    public Player(String name) {
        this.name = name;
        this.tileDeck = new Tile[7];
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public Tile[] getTileDeck() {
        return tileDeck;
    }

    /**
     * Counts the empty spaces in the tiledeck and returns it
     * @return # of empty spots
     */
    public int EmptySpotsInDeck(){
        int result = 0;
        for (int i = 0; i < 7; i++){
            if (tileDeck[i] == null){
                result++;
            }
        }
        return result;
    }

    /**
     * @requires tiles.length <= EmptySpotsInDeck()
     * Gives tiles to player to add to their
     * @param tiles tile array of tiles to give player
     */
    public void giveTiles(Tile[] tiles){
        for (Tile tile : tiles){
            for (int i = 0; i < 7; i++){
                if (tileDeck[i] == null){
                    tileDeck[i] = tile;
                }
            }
        }
    }

    public abstract String[] determineMove(Board board, TextBoardRepresentation tui);

}
