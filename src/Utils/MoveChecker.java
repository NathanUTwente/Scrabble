package Utils;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
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

        if (direction.equals("RIGHT")){
            String word = getWordTillEmpty(col, row, RIGHT);
            if (scrabbleWordChecker.isValidWord(word) == null){
                return word;
            } else {
                lastMovePoints += calculatePoints(word, getDifference(word, lettersUsed));
                for (int i = 0; i < word.length(); i++) {
                    if (lettersUsed.contains(boardCopy.getSquare(col + i, row).getTile().getTileLetter())) {
                        String wordToCheck = getWordTillEmpty(col + i, row, DOWN);
                        if (wordToCheck.length() > 1) {
                            if (scrabbleWordChecker.isValidWord(wordToCheck) == null) {
                                return wordToCheck;
                            }
                            lastMovePoints += calculatePoints(wordToCheck, "");
                        }
                    }
                }
            }
        } else {
            String word = getWordTillEmpty(col, row, DOWN);
            if (scrabbleWordChecker.isValidWord(word) == null){
                return word;
            } else {
                for (int i = 0; i < word.length(); i++) {
                    String wordToCheck = getWordTillEmpty(col, row + i, RIGHT);
                    if (wordToCheck.length() > 1) {
                        if (scrabbleWordChecker.isValidWord(wordToCheck) == null) {
                            return wordToCheck;
                        }
                    }
                }
            }
        }
        return null;
    }

    public int getLastMovePoints(){
        return lastMovePoints;
    }

    public String getDifference(String inThis, String notThis){
        String dif = "";
        for (String l : inThis.split("")){
            if (!notThis.contains(l)){
                dif += l;
            }
        }
        return dif;
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
                System.out.println(checkRow);
                checkCol--;
                System.out.println(checkRow);
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

    public int calculatePoints(String word, String toExclude){
        int score = 0;
        for (String l : word.split("")){
            if (!toExclude.contains(l)) {
                score += TileBag.GetPointOfTile(l);
            }
        }
        return score;
    }
}
