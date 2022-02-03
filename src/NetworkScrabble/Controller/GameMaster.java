package NetworkScrabble.Controller;

import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.Model.Game;
import NetworkScrabble.Model.PlayerModels.HumanPlayer;
import NetworkScrabble.Model.PlayerModels.Player;
import NetworkScrabble.View.TextBoardRepresentation;
import Utils.Exceptions.InvalidMoveException;
import Utils.Exceptions.TileBagEmptyException;
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
            boolean skip = false;
            boolean validMove = false;
            while (!validMove){
                try {
                    move = currentPlayer.determineMove(game.getBoard(), tui);
                    if (move[0].equals("SKIP")){
                        if (game.getTileBag().tilesLeftInBag() > 0) {
                            swapTiles(currentPlayer, move[1]);
                            skip = true;
                        } else {
                            throw new TileBagEmptyException("The tile bag is empty, you cannot swap tiles");
                        }
                    } else {
                        moveChecker.checkMove(move, game.getBoard());
                    }
                    validMove = true;
                } catch (InvalidMoveException e){
                    System.out.println(e.getMessage());
                }
            }
            if (!skip) {
                currentPlayer.removeTiles(getTilesToRemove(move));
                game.playMove(move);
                game.updatePoints(currentPlayer, moveChecker.getLastMovePoints());
            }
            if (game.getTileBag().tilesLeftInBag() > 0) {
                ArrayList<Tile> newTiles = game.getTileBag().getTilesForPlayer(currentPlayer);
                currentPlayer.giveTiles(newTiles);
            } else {
                System.out.println("The tile bag is now empty, no more tile swaps are possible");
            }
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

    public void swapTiles(Player player, String tilesToSwap){
        String[] tilesToRemove = getTilesToRemove(new String[]{"", "", tilesToSwap});
        player.removeTiles(tilesToRemove);
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
