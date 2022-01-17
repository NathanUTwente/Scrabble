package Scrabble.Model;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.View.TextBoardRepresentation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private Player[] players;
    private int playerTurn;
    private Board board;
    private Map<Player, Integer> scores;


    public Game(Player[] players) {
        board = new Board();
        this.players = players;
        scores = new HashMap<>();
        for (Player player : players){
            scores.put(player, 0);
        }
        //Should randomise this later
        playerTurn = 0;
    }

    public Board getBoard() {
        return board;
    }

    public boolean gameOver(){
        return false;
    }

    public Player getNextPlayer(){
        Player nextPlayer = players[playerTurn];
        playerTurn++;
        if (playerTurn >= players.length){
            playerTurn = 0;
        }
        return nextPlayer;
    }

    public void playMove(String[] move){

    }

    public void updatePoints(Player player, int points){

    }

}
