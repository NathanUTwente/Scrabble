package Utils;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.TileBag;

public class MoveChecker {
    private int lastMovePoints = 0;
    private Board boardCopy;
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


            String word = getWordTillEmpty(col, row, rightDownFirst);
            if (scrabbleWordChecker.isValidWord(word) == null){
                return word;
            } else {
                int appendCol = 0;
                int appendRow = 0;
                lastMovePoints += calculatePoints(word, lettersUsed);
                for (int i = 0; i < word.length(); i++) {
                    if (rightDownFirst.equals(RIGHT)){
                        appendCol = i;
                    } else {
                        appendRow = i;
                    }
                    String test = boardCopy.getSquare(col + appendCol, row + appendRow).getTile().getTileLetter();
                    String test2 = lettersUsed.split("")[i];
                    if (!(lettersUsed.split("")[i].equals("."))) {
                        String wordToCheck = getWordTillEmpty(col + appendCol, row + appendRow, rightDownSecond);
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

    public String getWordTillEmpty(int col, int row, String direction){
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
            word += boardCopy.getSquare(checkCol, checkRow).getTile().getTileLetter();
            checkCol += additionCol;
            checkRow += additionRow;
        }
        return word;
    }

    public int calculatePoints(String word, String lettersUsed){
        int score = 0;
        for (int i = 0; i < word.length(); i++){
                if (!lettersUsed.split("")[i].equals(".")){
                    score += TileBag.GetPointOfTile(word.split("")[i]);
                }
        }
        return score;
    }

    public int calculatePoints(String word){
        int score = 0;
        for (int i = 0; i < word.length(); i++){
            score += TileBag.GetPointOfTile(word.split("")[i]);
        }
        return score;
    }
}
