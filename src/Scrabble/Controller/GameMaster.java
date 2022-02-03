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
    }

    public GameMaster() {
        this.tui = new TextBoardRepresentation();
        this.moveChecker = new MoveChecker();
    }

    public void setUpGame(){
        Player[] players = new Player[]{new HumanPlayer("Nathan"), new HumanPlayer("Lejla")};
        setUpGame(players);
    }

    public void setUpGame(Player[] players){
        game = new Game(players);
        for (Player player : players){
            player.giveTiles(game.getTileBag().getTilesForPlayer(player));
        }
        runGame();
    }

    public void runGame(){
        while (!game.gameOver()){
            Player currentPlayer = game.getNextPlayer();
            tui.update(game.getBoard());
            tui.updatePlayerDeck(currentPlayer);
            String[] move = null;
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
        }
        tui.displayResults(game);
        if (playAgain(game)){
            System.out.println("Players voted to play again, good luck");
            setUpGame(game.getPlayers());
        } else {
            System.out.println("One or more players decided not to play again");
        }
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

    public boolean playAgain(Game game){
        boolean result = true;
        for (Player player : game.getPlayers()){
            if (!tui.wantToPlayAgain(player)){
                result = false;
            }
        }
        return result;
    }
}
