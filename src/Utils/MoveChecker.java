package Utils;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Square;
import Scrabble.Model.TileBag;

import java.util.ArrayList;

public class MoveChecker {
    private int lastMovePoints = 0;
    private Board boardCopy;
    private boolean firstDone = false;
    private InMemoryScrabbleWordChecker scrabbleWordChecker = new InMemoryScrabbleWordChecker();
    private static final String DOWN = "D";
    private static final String RIGHT = "R";

    public MoveChecker() {}

    /**
     * This method will check if the move is valid and return the result
     * @param move to be checked
     * @param board to check the move on
     * @return true if the move is valid
     */
    public String checkMove(String[] move, Board board){
        this.boardCopy = board.deepCopy();
        lastMovePoints = 0;
        String[] moveDetails = Board.placeMove(move, boardCopy);
        String direction = moveDetails[0];
        int col = Integer.parseInt(moveDetails[1]);
        int row = Integer.parseInt(moveDetails[2]);
        String lettersUsed = moveDetails[3];
        if (moveDetails[4].equals("F")){
            return "Full Stop";
        }

        String rightDownFirst;
        String rightDownSecond;
        if (direction.equals("RIGHT")){
            rightDownFirst = RIGHT;
            rightDownSecond = DOWN;
        } else {
            rightDownFirst = DOWN;
            rightDownSecond = RIGHT;
        }


            String[] wordTillEmpty = getWordTillEmpty(col, row, rightDownFirst);
            String word = wordTillEmpty[0];
            if (wordTillEmpty[1].equals("F")){
                return word;
            }
            if (scrabbleWordChecker.isValidWord(word) == null){
                return word;
            } else {
                int appendCol = 0;
                int appendRow = 0;
                lastMovePoints += calculatePoints(word, lettersUsed, col, row, rightDownFirst);
                for (int i = 0; i < word.length(); i++) {
                    if (rightDownFirst.equals(RIGHT)){
                        appendCol = i;
                    } else {
                        appendRow = i;
                    }
                    String test = boardCopy.getSquare(col + appendCol, row + appendRow).getTile().getTileLetter();
                    String test2 = lettersUsed.split("")[i];
                    if (!(lettersUsed.split("")[i].equals("."))) {
                        String wordToCheck = getWordTillEmpty(col + appendCol, row + appendRow, rightDownSecond)[0];
                        if (wordToCheck.length() > 1) {
                            if (scrabbleWordChecker.isValidWord(wordToCheck) == null) {
                                return wordToCheck;
                            }
                            lastMovePoints += calculatePoints(wordToCheck);
                        }
                    }
                }
            }
        return null;
    }

    public int getLastMovePoints(){
        return lastMovePoints;
    }

    public String[] getWordTillEmpty(int col, int row, String direction){
        ArrayList<Boolean> adjacent = new ArrayList<>();
        String adjacentResult = "F";
        String word = "";
        int additionCol = 0;
        int additionRow = 0;
        int checkCol = col;
        int checkRow = row;
        switch (direction){
            case DOWN:
                additionRow = 1;
                break;
            case RIGHT:
                additionCol = 1;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        //Find start
        if (additionRow == 1){
            while (boardCopy.isField(checkCol, checkRow - 1) && !boardCopy.isEmpty(checkCol, checkRow - 1)){
                checkRow--;
            }
        } else {
            while (boardCopy.isField(checkCol - 1, checkRow) && !boardCopy.isEmpty(checkCol - 1, checkRow)){
                checkCol--;
            }
        }

        //Get word
        while (boardCopy.isField(checkCol, checkRow) && !boardCopy.isEmpty(checkCol, checkRow)) {
            adjacent.add(adjacentNotEmpty(checkCol, checkRow, direction));
            word += boardCopy.getSquare(checkCol, checkRow).getTile().getTileLetter();
            checkCol += additionCol;
            checkRow += additionRow;
        }

        //Decide whether at least one letter has an adjacent existing word
        for (boolean bool : adjacent){
            if (bool){
                adjacentResult = "T";
                break;
            }
        }

        return new String[]{word, adjacentResult};
    }

    public boolean adjacentNotEmpty(int col, int row, String exclude){
        if (!firstDone){
            firstDone = true;
            return true;
        }
        ArrayList<Boolean> tests = new ArrayList<>();
        if (!(exclude.equals(DOWN)) && boardCopy.isField(col, row + 1)) {
            tests.add(!boardCopy.isEmpty(col, row + 1));
        }
        if (!(exclude.equals(DOWN)) && boardCopy.isField(col, row - 1)) {
            tests.add(!boardCopy.isEmpty(col, row -1));
        }
        if (!(exclude.equals(RIGHT)) && boardCopy.isField(col + 1, row)) {
            tests.add(!boardCopy.isEmpty(col + 1, row));
        }
        if (!(exclude.equals(RIGHT)) && boardCopy.isField(col - 1, row)) {
            tests.add(!boardCopy.isEmpty(col - 1, row));
        }
        boolean notEmpty = false;
        for (boolean bool : tests){
            if (bool == true){
                notEmpty = true;
                break;
            }
        }
        return notEmpty;
    }

    public int calculatePoints(String word, String lettersUsed, int col, int row, String direction){
        int score = 0;
        int wordMultiplier = 1;
        for (int i = 0; i < word.length(); i++){
            int letterMultiplier = 1;
            int eachCol = 0;
            int eachRow = 0;
            if (direction.equals(RIGHT)){
                eachCol += i;
            } else {
                eachRow += i;
            }
                if (!lettersUsed.split("")[i].equals(".")){
                    Square.SpecialType specialType = boardCopy.getSquare(col + eachCol, row + eachRow).getSpecialType();
                    if (specialType != null) {
                        switch (specialType) {
                            case CENTRE:
                            case DOUBLE_WORD:
                                wordMultiplier = 2;
                                break;
                            case TRIPLE_WORD:
                                wordMultiplier = 3;
                                break;
                            case DOUBLE_LETTER:
                                letterMultiplier = 2;
                                break;
                            case TRIPLE_LETTER:
                                letterMultiplier = 3;
                                break;
                        }
                    }
                    int letterScore = (TileBag.GetPointOfTile(word.split("")[i])) * letterMultiplier;
                    score += letterScore;

                }
        }
        return score*wordMultiplier;
    }

    public int calculatePoints(String word){
        int score = 0;
        for (int i = 0; i < word.length(); i++){
            score += TileBag.GetPointOfTile(word.split("")[i]);
        }
        return score;
    }
}
