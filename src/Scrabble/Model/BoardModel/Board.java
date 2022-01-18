package Scrabble.Model.BoardModel;

import Scrabble.Model.TileBag;

import java.util.HashMap;

public class Board {
    public static final int DIM = 15;

    // Square[column][row]
    private Square[][] field;

//    public static void main(String[] args) {
//        Board board = new Board();
//        for (int col = 0; col < DIM; col++){
//            for (int row = 0; row < DIM; row++){
//                System.out.println(board.field[col][row].toString());;
//            }
//        }
//    }

    /**
     * Constructs a board with a playing field of 15x15 Squares with special squares included
     * @ensures all squares are empty
     */
    public Board(){
        field = new Square[DIM][DIM];
        makeAllSpecialSquares();
        for (int col = 0; col < DIM; col++){
            for (int row = 0; row < DIM; row++){
                if (field[col][row] == null) {
                    field[col][row] = new Square(new int[]{col, row});
                }
            }
        }
    }

    /**
     * Creates an exact copy of the boards current state with new objects
     * @ensures the result is a new object, not this object
     * @ensures all squares and tiles in the returned board are identical to this board
     */
    public Board deepCopy(){
        Board newBoard = new Board();
        for (int col = 0; col < DIM; col++) {
            for (int row = 0; row < DIM; row++) {
                Tile toCopy = field[col][row].getTile();
                if (toCopy != null) {
                    newBoard.setField(col, row, new Tile(toCopy.getTileType(), toCopy.getPoints()));
                }
            }
        }
        return newBoard;
    }

    /**
     * Calculates the index in the two-dimensional array
     * @param row the row on the board, as an int
     * @param col the column on the board, as a char
     * @return returns the index in a list
     */
    public int[] index(char col, int row){
        int ascii = (int) col;

        return new int[]{ascii - 65, row - 1};
    }

    /**
     * returns true if the provided index is a valid space on the board
     * @ensures true if the given row and col is a space within the DIM*DIM spaces on the board
     */
    public boolean isField(int col, int row){
        return (0 <= col && col < DIM) && (0 <= row && row < DIM);
    }

    /**
     * Returns the square object at the given location
     * @requires (row, col) is a valid field on the board
     * @param row the row of the square
     * @param col the col of the square
     * @return the square at the given coordinates
     */
    public Square getSquare(int col, int row){
        return field[col][row];
    }

    /**
     * Returns true if the given location on the board does not have a letter played on it
     * @requires i to be a valid field index
     * @param row the row of the square
     * @param col the col of the square
     * @return true if the square hasn't had a letter played on it
     */
    public boolean isEmpty(int col, int row){
        Square square = field[col][row];
        if (square.getTile() == null){
            return true;
        } else {
            return false;
        }
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
        for (int col = 0; col < DIM; col++){
            for (int row = 0; row < DIM; row++){
                field[col][row].setTile(null);
            }
        }
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
    public void setField(int col, int row, Tile tile){
        field[col][row].setTile(tile);
    }

    /**
     * Creates and fills in all special squares on the board
     */
    public void makeAllSpecialSquares(){

        HashMap<Integer, int[]> tripleWordSpaces = new HashMap<>();

        tripleWordSpaces.put(0,new int[]{0, 7, 14});
        tripleWordSpaces.put(7, new int[]{0, 14});
        tripleWordSpaces.put(14, new int[]{0, 7, 14});

        HashMap<Integer, int[]> doubleWordSpaces = new HashMap<>();

        doubleWordSpaces.put(1, new int[]{1, 13});
        doubleWordSpaces.put(2, new int[]{2, 12});
        doubleWordSpaces.put(3, new int[]{3, 11});
        doubleWordSpaces.put(4, new int[]{4, 10});
        doubleWordSpaces.put(10, new int[]{4, 10});
        doubleWordSpaces.put(11, new int[]{3, 11});
        doubleWordSpaces.put(12, new int[]{2, 12});
        doubleWordSpaces.put(13, new int[]{1, 13});


        HashMap<Integer, int[]> tripleLetterSpaces = new HashMap<>();

        tripleLetterSpaces.put(1, new int[]{5, 9});
        tripleLetterSpaces.put(5, new int[]{1, 5, 9, 13});
        tripleLetterSpaces.put(9, new int[]{1, 5, 9, 13});
        tripleLetterSpaces.put(13, new int[]{5, 9});


        HashMap<Integer, int[]> doubleLetterSpaces = new HashMap<>();

        doubleLetterSpaces.put(0, new int[]{3, 11});
        doubleLetterSpaces.put(2, new int[]{6, 8});
        doubleLetterSpaces.put(3, new int[]{0, 7, 14});
        doubleLetterSpaces.put(6, new int[]{2, 6, 8, 12});
        doubleLetterSpaces.put(7, new int[]{3, 11});
        doubleLetterSpaces.put(8, new int[]{2, 6, 8, 12});
        doubleLetterSpaces.put(11, new int[]{0, 7, 14});
        doubleLetterSpaces.put(12, new int[]{6, 8});
        doubleLetterSpaces.put(14, new int[]{3, 11});

        //Creates centre square
        field[7][7] = new Square(new int[]{7, 7}, Square.SpecialType.CENTRE);

        //Creates all triple letter squares
        for (int col : tripleLetterSpaces.keySet()){
            for (int row : tripleLetterSpaces.get(col)){
                field[col][row] = new Square(new int[]{col, row}, Square.SpecialType.TRIPLE_LETTER);
            }
        }

        //Creates all triple word squares
        for (int col : tripleWordSpaces.keySet()){
            for (int row : tripleWordSpaces.get(col)){
                field[col][row] = new Square(new int[]{col, row}, Square.SpecialType.TRIPLE_WORD);
            }
        }

        //Creates all double letter squares
        for (int col : doubleLetterSpaces.keySet()){
            for (int row : doubleLetterSpaces.get(col)){
                field[col][row] = new Square(new int[]{col, row}, Square.SpecialType.DOUBLE_LETTER);
            }
        }

        //Creates all double word squares
        for (int col : doubleWordSpaces.keySet()){
            for (int row : doubleWordSpaces.get(col)){
                field[col][row] = new Square(new int[]{col, row}, Square.SpecialType.DOUBLE_WORD);
            }
        }
    }

    public static String[] placeMove(String[] move, Board board){

        String position = move[0];
        String direction = move[1];
        String letters = move[2];
        char col = position.charAt(0);
        String row = position.split("")[1];
        if (position.split("").length > 2){
            row += position.split("")[2];
        }
        int[] location = board.index(col, Integer.parseInt(row));
        int addition = 0;
        String[] moveToReturn = new String[]{direction, "" + location[0], "" + location[1], letters, "T"};
        if (board.isEmpty(location[0], location[1]) || letters.split("")[0].equals(".")) {
            for (String eachLetter : letters.split("")) {
                if (direction.equals("RIGHT")) {
                    if (eachLetter.equals(".")) {
                        if (board.isEmpty(location[0] + addition, location[1])){
                            moveToReturn[4] = "F";
                            break;
                        }
                        addition++;
                        continue;
                        }
                    board.setField(location[0] + addition, location[1], new Tile(TileBag.stringToTile(eachLetter), 1));
                } else {
                    if (eachLetter.equals(".")) {
                        if (board.isEmpty(location[0], location[1] + addition)) {
                            moveToReturn[4] = "F";
                            break;
                        }
                        addition++;
                        continue;
                    }
                    board.setField(location[0], location[1] + addition, new Tile(TileBag.stringToTile(eachLetter), 1));
                }
                addition++;
            }
        }
        return moveToReturn;
    }
}
