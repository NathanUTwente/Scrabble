package Scrabble.View;

import Scrabble.Controller.GameMaster;
import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.HumanPlayer;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.Model.TileBag;
import Utils.QuickSort;

import java.util.*;

public class TextBoardRepresentation {
    private final String[] POSITIONS = new String[]{"1st", "2nd", "3rd", "4th"};

    public static void main(String[] args) {
//        TextBoardRepresentation tui = new TextBoardRepresentation();
//        HashMap<Player, Integer> scores = new HashMap<>();
//        scores.put(new HumanPlayer("Bob"), 87);
//        scores.put(new HumanPlayer("Rob"), 15);
//        scores.put(new HumanPlayer("Tim"), 112);
//        scores.put(new HumanPlayer("Jim"), 3);
//        tui.displayScores(scores);
    }

    /**
     * Method updates the TUI with the current board state
     *
     * @param board board to be displayed
     */
    public void update(Board board) {
        System.out.println(BoardPrinter.createBoard(board));
    }

    /**
     * Gets input from specified player about move
     *
     * @param player to get move from
     * @return the move instructions parsed into a string array
     */
    public String[] getMove(Player player, Board board) {
        System.out.println(player.getName() + " please enter your move.\nIn the form [Start Column][Start row] [Direction] [Word]\nEg. F6 RIGHT HELLO\n '_' represents a blank tile");
        Scanner scanner = new Scanner(System.in);
        String[] input;
        String line = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            input = line.toUpperCase(Locale.ROOT).split(" ");
            if (checkFormat(input)) {
                if (playerHasLetters(player, input)) {
                    if (input[2].contains("_")) {
                        String[] blankReplace = getBlank(input[2]);
                        input = replaceUnderscores(input, blankReplace);
                    }
                    return input;
                } else {
                    System.out.println("You do not have the required tiles for this move, please try again");
                }
            } else {
                System.out.println("Wrong format, please retype your move");
            }
        }
        return null;
    }

    /**
     * Updates and displays the tile deck for the current player
     */
    public void updatePlayerDeck(Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        Tile[] playerTiles = player.getTileDeck();
        stringBuilder.append("Player " + player.getName() + ", this is your current tile deck\n    |");
        for (Tile tile : playerTiles) {
            stringBuilder.append(" [" + tile.getTileLetter() + "] ");
        }
        stringBuilder.append("|");
        System.out.println(stringBuilder.toString());
    }

    /**
     * Displays the results at the end of the given game
     *
     * @param game the game to display results of
     * @requires game.isOver == true
     */
    public void displayResults(Game game) {

    }

    public boolean checkFormat(String[] input) {
        boolean result = true;
        ArrayList<Boolean> tests = new ArrayList<>();
        if (input.length != 3){
            return false;
        }
        tests.add(input[0].length() <= 3);
        tests.add(input[1].toUpperCase(Locale.ROOT).equals("RIGHT") || input[1].toUpperCase(Locale.ROOT).equals("DOWN"));
        tests.add(!Character.isDigit(input[0].charAt(0)));
        tests.add(Character.isDigit(input[0].charAt(1)));
        if (input[0].length() == 3) {
            tests.add(Character.isDigit(input[0].charAt(2)));
        }

        for (Boolean bool : tests) {
            if (!bool) {
                result = false;
                break;
            }
        }

        return result;
    }


    public boolean playerHasLetters(Player player, String[] move) {
        Tile[] tileBagClone = player.cloneOfTiledeck();
        HashMap<String, Integer> lettersInputed = new HashMap<>();
        HashMap<String, Integer> lettersTilebag = new HashMap<>();
        String[] moveSplit = move[2].split("");
        for (String l : moveSplit) {
            if (!lettersInputed.containsKey(l)) {
                lettersInputed.put(l, 1);
            } else {
                lettersInputed.put(l, lettersInputed.get(l) + 1);
            }
        }
        for (Tile t : tileBagClone) {
            String l = t.getTileLetter();
            if (!lettersTilebag.containsKey(l)) {
                lettersTilebag.put(l, 1);
            } else {
                lettersTilebag.put(l, lettersTilebag.get(l) + 1);
            }
        }

            for (String m : lettersInputed.keySet()){
                if (lettersTilebag.containsKey(m)){
                    if (lettersInputed.get(m) > lettersTilebag.get(m)){
                        System.out.println("you don't have the facilities for that big man.");
                        return false;
                    }
                } else {
                    if (!m.equals(".")) {
                        return false;
                    }
                }
            }
            return true;
        }

        public String[] getBlank(String letters) {
            int blankCount = 0;
            for (String l : letters.split("")) {
                if (l.equals("_")) {
                    blankCount++;
                }
            }
            String[] blanks = new String[blankCount];
            for (int i = 0; i < blankCount; i++) {
                System.out.println("Enter letter of choice for blank tile #" + (i + 1));
                boolean valid = false;
                Scanner scanner = new Scanner(System.in);
                while (!valid) {
                    if (scanner.hasNextLine()) {
                        String res = scanner.nextLine();
                        if ((res.length() == 1) && (!res.equals("_")) && (TileBag.stringToTile(res.toUpperCase()) != null)) {
                            blanks[i] = res.toUpperCase();
                            valid = true;

                        } else {
                            System.out.println("Invalid letter");
                        }
                    }
                }
            }
            return blanks;
        }

        public String[] replaceUnderscores(String[] move, String[] replacements){
            int replacementInd = 0;
            String moveCopy = "";
            for (String l : move[2].split("")){
                if (l.equals("_")){
                    moveCopy += replacements[replacementInd];
                    replacementInd++;
                } else {
                    moveCopy += l;
                }
            }
            move[2] = moveCopy;
            return move;
        }

        public void displayScores(Map<Player , Integer> scores){
            int[] justScores = new int[scores.values().size()];
            int index = 0;
            for (int i : scores.values()){
                justScores[index] = i;
                index++;
            }
            QuickSort.qsort(justScores);
            System.out.println("The current scores are as follows: ");
            int pos = 0;
            for (int i = justScores.length - 1; i >= 0; i--){
                for (Player p : scores.keySet()){
                    if (justScores[i] == scores.get(p)){
                        System.out.println(POSITIONS[pos] + " : " + p.getName() + " with " + justScores[i] + " points");
                        pos++;
                    }
                }
            }
        }

}
