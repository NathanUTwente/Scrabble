package Scrabble.Controller;

import Scrabble.Model.BoardModel.Tile;
import Scrabble.Model.Game;
import Scrabble.Model.PlayerModels.HumanPlayer;
import Scrabble.Model.PlayerModels.Player;
import Scrabble.View.TextBoardRepresentation;
import Utils.Exceptions.InvalidMoveException;
import Utils.MoveChecker;

import java.util.ArrayList;

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
        for (Player player : players){
            player.giveTiles(game.getTileBag().getTilesForPlayer(player));
        }
    }

    public void runGame(){
        while (!game.gameOver()){
            Player currentPlayer = game.getNextPlayer();
            tui.update(game.getBoard());
            tui.updatePlayerDeck(currentPlayer);
            String[] move = null;
            //needs modification to check for exceptions
            boolean validMove = false;
            while (!validMove){
                try {
                    move = currentPlayer.determineMove(game.getBoard(), tui);
                    moveChecker.checkMove(move, game.getBoard());
                    validMove = true;
                } catch (InvalidMoveException e){
                    System.out.println(e.getMessage());
                }
            }
            currentPlayer.removeTiles(getTilesToRemove(move));
            game.playMove(move);
            game.updatePoints(currentPlayer, moveChecker.getLastMovePoints());

            ArrayList<Tile> newTiles = game.getTileBag().getTilesForPlayer(currentPlayer);
            currentPlayer.giveTiles(newTiles);
            tui.displayScores(game.getScores());
            //get number of remaining tiles in bag here


        }
        tui.displayResults(game);
    }


    public String[] getTilesToRemove(String[] move){
        String[] toRemoveFromPlayer = new String[move[2].length()];
        for (String l : move[2].split("")){
            for (int i = 0; i < toRemoveFromPlayer.length; i++){
                if (toRemoveFromPlayer[i] == null){
                    toRemoveFromPlayer[i] = l;
                    break;
                }
            }
        }
        return toRemoveFromPlayer;
    }






}
