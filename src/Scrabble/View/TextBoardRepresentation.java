package Scrabble.View;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.HumanPlayer;
import Scrabble.Model.PlayerModels.Player;

import java.util.ArrayList;
import java.util.Locale;
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
        System.out.println(player.getName() + " please enter your move.\nIn the form [Start Column][Start row] [Direction] [Word]\nEg. F6 RIGHT HELLO");
        Scanner scanner = new Scanner(System.in);
        String[] input;
        String line = "";
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            input = line.toUpperCase(Locale.ROOT).split(" ");
            if (checkFormat(input)){
                return input;
            } else {
                System.out.println("Wrong format, please retype your move");
            }
        }
        return null;
    }

    /**
     * Updates and displays the tile deck for the current player
     */
    public void updatePlayerDeck(Player player){
        StringBuilder stringBuilder = new StringBuilder();
        Tile[] playerTiles = player.getTileDeck();
        stringBuilder.append("Player " + player.getName() + ", this is your current tile deck\n    |");
        for (Tile tile : playerTiles){
            stringBuilder.append(" [" + tile.getTileLetter() + "] ");
        }
        stringBuilder.append("|");
        System.out.println(stringBuilder.toString());
    }

    /**
     * Displays the results at the end of the given game
     * @requires game.isOver == true
     * @param game the game to display results of
     */
    public void displayResults(Game game){

    }

    public boolean checkFormat(String[] input){
        boolean result = true;
        ArrayList<Boolean> tests = new ArrayList<>();
        tests.add(input.length == 3);
        tests.add(input[0].length() <= 3);
        tests.add(input[1].toUpperCase(Locale.ROOT).equals("RIGHT") || input[1].toUpperCase(Locale.ROOT).equals("DOWN"));
        tests.add(!Character.isDigit(input[0].charAt(0)));
        tests.add(Character.isDigit(input[0].charAt(1)));
        if (input[0].length() == 3){
            tests.add(Character.isDigit(input[0].charAt(2)));
        }

        for (Boolean bool : tests){
            if (!bool){
                result = false;
                break;
            }
        }
        return result;
    }

}
