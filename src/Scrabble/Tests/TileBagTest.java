package Scrabble.Tests;

import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.HumanPlayer;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.Model.TileBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TileBagTest {



    @Test
    void testBeginAmountOfTiles(){
        Player[] players = new Player[]{new HumanPlayer("Lejla"), new HumanPlayer("Nathan")};
        Game game = new Game(players);
        Player playerOne = players[0];
        Player playerTwo = players[1];
        Tile[] tileDeckPlayerOne = playerOne.getTileDeck();
        Tile[] tileDeckPlayerTwo = playerTwo.getTileDeck();
        assertEquals(7, tileDeckPlayerOne.length);
        assertEquals(7, tileDeckPlayerTwo.length);
    }




}