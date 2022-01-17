package Scrabble.Model.BoardModel;

public class Board {
    public static final int DIM = 15;

    private Square[][] field;

    /**
     * Constructs a board with a playing field of 15x15 Squares
     * @ensures all squares are empty
     */
    public Board(){
    }

    /**
     * Creates an exact copy of the boards current state with new objects
     * @ensures the result is a new object, not this object
     * @ensures all squares and tiles in the returned board are identical to this board
     */
    public Board deepCopy(){
        return null;
    }

    /**
     * Calculates the index in the two-dimensional array
     * @param row the row on the board, as an int
     * @param col the column on the board, as a char
     * @return returns the index in a list
     */
    public int[] index(int row, char col){
        return  null;
    }

    /**
     * returns true if the provided index is a valid space on the board
     * @ensures true if the given row and col is a space within the DIM*DIM spaces on the board
     */
    public boolean isField(int row, int col){
        return false;
    }

    /**
     * Returns the square object at the given location
     * @requires (row, col) is a valid field on the board
     * @param row the row of the square
     * @param col the col of the square
     * @return the square at the given coordinates
     */
    public Square getSquare(int row, int col){
        return null;
    }

    /**
     * Returns true if the given location on the board does not have a letter played on it
     * @requires i to be a valid field index
     * @param row the row of the square
     * @param col the col of the square
     * @return true if the square hasn't had a letter played on it
     */
    public boolean isEmpty(int row, int col){
        return false;
    }

    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or the bag has no new letters and any player is out of letters.
     * @ensures true if the bag has no new letters and any player is out of letter or when there is a winner
     * @return true if the game is over
     */
    public boolean gameOver(){
        return false;
    }

    /**
     * Empties all fields of this board (i.e., let all squares be empty).
     * @ensures all squares are EMPTY
     */
    public void reset(){
    }

    /**
     * Places the given tile on the square at the location given by (row, col)
     * @requires (row, col) to be a valid square
     * @requires square (row, col) to be empty
     * @ensures Tile tile placed on Square (row, col)
     * @param row the square's row
     * @param col the square's column
     * @param tile the tile to be placed
     */
    public void setField(int row, int col, Tile tile){

    }
}
