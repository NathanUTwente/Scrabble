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

    public static void main(String[] args) {
        GameMaster gameMaster = new GameMaster();
        gameMaster.setUpGame();
        gameMaster.runGame();
    }

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
            String[] move = currentPlayer.determineMove(game.getBoard(), tui);
            String badWord = null;
            while ((badWord  = moveChecker.checkMove(move, game.getBoard())) != null){
                System.out.println(badWord + " is not a word dumbass");
                move = currentPlayer.determineMove(game.getBoard(), tui);
            }
            game.playMove(move);
            System.out.println(moveChecker.getLastMovePoints());
//            game.updatePoints(currentPlayer, moveChecker.getLastMovePoints());
            //Tile[] newTiles = game.tileBag.getNewTiles(currentPlayer)
//            currentPlayer.giveTiles(newTiles);
//            tui.updatePlayerDeck(currentPlayer.getTileDeck());


        }
        tui.displayResults(game);
    }




}
