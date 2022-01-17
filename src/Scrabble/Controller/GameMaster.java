package Scrabble.Controller;

import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.HumanPlayer;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.View.TextBoardRepresentation;
import Utils.MoveChecker;

public class GameMaster {

    private Game game;
    private TextBoardRepresentation tui;
    private MoveChecker moveChecker;

    public GameMaster() {
        this.tui = new TextBoardRepresentation();
        this.moveChecker = new MoveChecker();
    }

    public void setUpGame(){
        Player[] players = new Player[]{new HumanPlayer("Nathan"), new HumanPlayer("Lejla")};
        game = new Game(players);
    }

    public void runGame(){
        while (!game.gameOver()){
            Player currentPlayer = game.getNextPlayer();
            tui.update(game.getBoard());
            String[] move = tui.getMove(currentPlayer, game.getBoard());
            if (moveChecker.checkMove(move, game.getBoard())){
                game.playMove(move);
                game.updatePoints(currentPlayer, moveChecker.getLastMovePoints());
                //Tile[] newTiles = game.tileBag.getNewTiles(currentPlayer)
                tui.updatePlayerDeck(currentPlayer.getTileDeck());
            } else {
                tui.getMove(currentPlayer, game.getBoard());
            }
        }
        tui.displayResults(game);
    }




}
