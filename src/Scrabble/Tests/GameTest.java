package Scrabble.Tests;

import Scrabble.Controller.GameMaster;
import Scrabble.Model.PlayerModels.HumanPlayer;
import org.junit.jupiter.api.Test;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.Model.Game;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    void updatePointsTest() {
        //scores.put(player, scores.get(player) + points);
        Player[] players = new Player[]{new HumanPlayer("Lejla"), new HumanPlayer("Nathan")};
        Game game = new Game(players);
        Player playerOne = players[0];
        game.updatePoints(playerOne, 4);
        assertEquals(4, game.getScores().get(playerOne));
        }

    @Test
    void nextPlayerTest(){
        Player[] players = new Player[]{new HumanPlayer("Lejla"), new HumanPlayer("Nathan")};
        Game game = new Game(players);
        Player playerOne = players[0];
        Player playerTwo = players[1];
        Player nextPlayer = game.getNextPlayer();
        if (nextPlayer.equals(playerOne)){
            assertEquals(playerTwo, game.getNextPlayer());
        } else {
            assertEquals(playerOne, game.getNextPlayer());
        }
    }
}
