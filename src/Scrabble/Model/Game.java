package Scrabble.Model;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.PlayerModels.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        Random random = new Random();
        int randomOrder = random.nextInt(2);
        playerTurn = randomOrder;
    }

    /**
     * gets the board
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * returns the players
     * @return players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * gets the next player that has to play
     * @return Player
     */
    public Player getNextPlayer(){
        Player nextPlayer = players[playerTurn];
        playerTurn++;
        if (playerTurn >= players.length){
            playerTurn = 0;
        }
        return nextPlayer;
    }

    /**
     * places the move on the board
     * @param move
     */
    public void playMove(String[] move){
        Board.placeMove(move, board);
    }

    /**
     * gets the points from the move the player played and adds them to the total score
     * @param player
     * @param points
     */
    public void updatePoints(Player player, int points){
        scores.put(player, scores.get(player) + points);

    }

    /**
     * gets the scores
     * @return scores
     */
    public Map<Player, Integer> getScores() {
        return scores;
    }

    public TileBag getTileBag() {
        return tileBag;
    }

    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or the bag has no new letters and any player is out of letters.
     * @ensures true if the bag has no new letters and any player is out of letter or when there is a winner
     * @return true if the game is over
     */
    public boolean gameOver(){
        boolean result = false;
        if (tileBag.tilesLeftInBag() == 0){
            for (Player player : players){
                if (player.getTileDeck().length == 0){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
