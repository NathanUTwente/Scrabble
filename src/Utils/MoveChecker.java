package Utils;

import Scrabble.Model.BoardModel.Board;

public class MoveChecker {
    private int lastMovePoints = 0;
    private Board board;

    /**
     * This method will check if the move is valid and return the result
     * @param move to be checked
     * @param board to check the move on
     * @return true if the move is valid
     */
    public boolean checkMove(String[] move, Board board){
        lastMovePoints = 0;
        return true;
    }

    public int getLastMovePoints(){
        return lastMovePoints;
    }


}
