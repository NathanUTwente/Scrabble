package Scrabble.Model.PlayerModels;

import Scrabble.Model.BoardModel.Tile;

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

    /**
     * Gives tiles to player to add to their
     * @param tiles tile array of tiles to give player
     */
    public void giveTiles(Tile[] tiles){

    }
}
