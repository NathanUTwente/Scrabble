package Scrabble.Tests;

import Scrabble.Model.PlayerModels.HumanPlayer;
import org.junit.jupiter.api.Test;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.Model.Game;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    void updatePointsTest() {
        //scores.put(player, scores.get(player) + points);
        Player[] players = new Player[]{new HumanPlayer("Lejla"), new HumanPlayer("Nathan")};
        Game game = new Game(players);
        Player playerOne = players[0];
        Player playerTwo = players[1];
            if (playerOne.getPoints() == 4){
                game.updatePoints(playerOne,4);
                assertEquals(8, playerOne.getPoints());
        }
    }

    @Test
    void gameOverTest() {
        Player[] players = new Player[]{new HumanPlayer("Lejla"), new HumanPlayer("Nathan")};
        Game game = new Game(players);
        if (game.gameOver()){
            assertTrue(game.gameOver());
        }
    }
}