package Scrabble.View;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.Player;

public class TextBoardRepresentation {

    /**
     * Method updates the TUI with the current board state
     * @param board board to be displayed
     */
    public void update(Board board){
    }

    /**
     * Gets input from specified player about move
     * @param player to get move from
     * @return the move instructions parsed into a string array
     */
    public String[] getMove(Player player){
        return null;
    }

    /**
     * Updates and displays the tile deck for the current player
     */
    public void updatePlayerDeck(){
    }

    /**
     * Displays the results at the end of the given game
     * @requires game.isOver == true
     * @param game the game to d
     */
    public void displayResults(Game game){

    }

}
