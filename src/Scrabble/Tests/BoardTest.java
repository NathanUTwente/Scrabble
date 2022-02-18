package Scrabble.Tests;

import Scrabble.Model.BoardModel.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void newBoardIsEmpty(){
        Board board = new Board();
        for (int i = 0; i < 14; i++) {
            assertTrue(board.isEmpty(i+1, i+1));
        }
    }

    @Test
    void movePlacedRightSpot(){
        Board board = new Board();
        String move = "F6 RIGHT HELLO";
        String[] moveGesplit = move.split(" ");
        Board.placeMove(moveGesplit, board);
        assertEquals("H", board.getSquare(5, 5).getTile().getTileLetter());
        assertEquals("E", board.getSquare(6, 5).getTile().getTileLetter());
        assertEquals("L", board.getSquare(7, 5).getTile().getTileLetter());
        assertEquals("L", board.getSquare(8, 5).getTile().getTileLetter());
        assertEquals("O", board.getSquare(9, 5).getTile().getTileLetter());
    }
}

