package Scrabble.Tests;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {


//    void boardtest(){
//        Board board = new Board();
//        board.setField(5,6, Tile.TileType.B);
//        Board boardCopy = board.deepCopy();
//        boardCopy.setField(2,8, new Tile(Tile.TileType.B));
//        assertEquals(Tile.TileType.B, board.getSquare(5,6).getTile());
//        assertEquals(Tile.TileType.B, boardCopy.getSquare(5, 6).getTile());
//        assertEquals(Tile.TileType.E, boardCopy.getSquare(2,8).getTile());
//
//    }

    void newBoardIsEmpty(){
        Board board = new Board();
        for (int i = 0; i < 16; i++) {
                assertTrue(board.isEmpty(i+1, i+1));
        }



    }







}