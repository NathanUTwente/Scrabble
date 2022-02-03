package NetworkScrabble.Model.PlayerModels;

import NetworkScrabble.Model.BoardModel.Board;
import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.View.TextBoardRepresentation;

import java.util.ArrayList;

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
    public void giveTiles(ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            for (int i = 0; i < 7; i++){
                if (tileDeck[i] == null){
                    tileDeck[i] = tile;
                    break;
                }
            }
        }
    }
    public void removeTiles(String[] lettersToRemove){
        for (String l : lettersToRemove){
            for (int i = 0; i < this.tileDeck.length; i++){
                if (this.tileDeck[i] != null && l.equals(this.tileDeck[i].getTileLetter())){
                    this.tileDeck[i] = null;
                    break;
                }
            }
        }
    }

    public Tile[] cloneOfTiledeck(){
        return getTileDeck().clone();
    }

    public void removeTile(String letterToRemove) {
        Tile[] copyTD = cloneOfTiledeck();
                for (int i = 0; i < copyTD.length; i++) {
                    if (copyTD[i] != null && letterToRemove.equals(copyTD[i].getTileLetter())) {
                        copyTD[i] = null;
                        break;
                    }
                }
            }



    public abstract String[] determineMove(Board board, TextBoardRepresentation tui);








}
