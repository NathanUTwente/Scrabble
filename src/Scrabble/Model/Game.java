package Scrabble.Model;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
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
    private TileBag tileBag;


    public Game(Player[] players) {
        board = new Board();
        tileBag = new TileBag();
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
        Board.placeMove(move, board);
    }

    public void updatePoints(Player player, int points){
        scores.put(player, scores.get(player) + points);

    }

    public Map<Player, Integer> getScores() {
        return scores;
    }

    public TileBag getTileBag() {
        return tileBag;
    }
}
