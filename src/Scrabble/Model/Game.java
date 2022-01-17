package Scrabble.Model;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.View.TextBoardRepresentation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Game {
    private Player[] players;

    private Board board;

    private Map<Player, Integer> scores;


    public Game() {
    }

    public boolean gameOver(){
        return false;
    }

}
