package Scrabble.View;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.HumanPlayer;
import Scrabble.Model.PlayerModels.Player;

import java.util.Scanner;

public class TextBoardRepresentation {

    public static void main(String[] args) {
        TextBoardRepresentation textBoardRepresentation = new TextBoardRepresentation();
        textBoardRepresentation.getMove(new HumanPlayer("Jim"), new Board());
    }

    /**
     * Method updates the TUI with the current board state
     * @param board board to be displayed
     */
    public void update(Board board){
        System.out.println(BoardPrinter.createBoard(board));
    }

    /**
     * Gets input from specified player about move
     * @param player to get move from
     * @return the move instructions parsed into a string array
     */
    public String[] getMove(Player player, Board board){
        System.out.println(player.getName() + " please enter your move");
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
//            scanner.close();
            String[] move = line.split(" ");
            return move;
        }
        return null;
    }

    /**
     * Updates and displays the tile deck for the current player
     */
    public void updatePlayerDeck(Tile[] tiles){
    }

    /**
     * Displays the results at the end of the given game
     * @requires game.isOver == true
     * @param game the game to display results of
     */
    public void displayResults(Game game){

    }

}
