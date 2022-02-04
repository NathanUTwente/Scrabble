package NetworkScrabble.Controller;

import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.Model.Game;
import NetworkScrabble.Model.PlayerModels.HumanPlayer;
import NetworkScrabble.Model.PlayerModels.Player;
import NetworkScrabble.Model.TileBag;
import NetworkScrabble.Utils.Exceptions.InvalidMoveException;
import NetworkScrabble.Utils.Exceptions.TileBagEmptyException;
import NetworkScrabble.Utils.MoveChecker;
import NetworkScrabble.View.TextBoardRepresentation;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMaster {

    private Game game;
    private TextBoardRepresentation tui;
    private MoveChecker moveChecker;
    private boolean gamOver = false;

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
    }
    public Player getCurrentPlayer(){
        Player currentPlayer = game.getNextPlayer();
        tui.update(game.getBoard());
        return currentPlayer;
    }

    public int endOfMove(Player currentPlayer, String[] move){
        game.playMove(move);
        int lastPoints = moveChecker.getLastMovePoints();
        game.updatePoints(currentPlayer, lastPoints);
        return lastPoints;
    }

    public ArrayList<Tile> getNewTiles(Player currentPlayer){
        if (game.getTileBag().tilesLeftInBag() > 0) {
        ArrayList<Tile> newTiles = game.getTileBag().getTilesForPlayer(currentPlayer);
        return newTiles;
    } else {
        System.out.println("The tile bag is now empty, no more tile swaps are possible");
    }
        return null;
    }

    public void runGame(){
        while (!game.gameOver()) {
//            tui.updatePlayerDeck(currentPlayer);

//            if (!skip) {
//                currentPlayer.removeTiles(getTilesToRemove(move));
//            }

        }
        gameEnd();
    }

    public void processMove(Player currentPlayer, String[] move){
//        boolean skip = false;
        boolean validMove = false;
        while (!validMove) {

        }
    }

    public boolean isMoveValid(String[] move) throws InvalidMoveException {
        moveChecker.checkMove(move, game.getBoard());
        return true;
    }

    public int swapTiles(String[] move) throws TileBagEmptyException {
        if (game.getTileBag().tilesLeftInBag() > 0) {
            String[] tilesToRemove = getTilesToRemove(move);
            for (String t : tilesToRemove){
                game.getTileBag().addToBag(new Tile(TileBag.stringToTile(t), TileBag.GetPointOfTile(t)));
            }
            return tilesToRemove.length;

    } else {
        throw new TileBagEmptyException("The tile bag is empty, you cannot swap tiles");
    }
    }


    public void gameEnd(){
        tui.displayScores(game.getScores());
        tui.displayResults(game);
        if (playAgain(game)){
            System.out.println("Players voted to play again, good luck");
            setUpGame(game.getPlayers());
        } else {
            System.out.println("One or more players decided not to play again");
        }

    }

//    public void swapTiles(Player player, String tilesToSwap){
//        player.removeTiles(tilesToRemove);
//    }


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

    public Player[] makePlayers(ArrayList<String> playerNames){
        Player[] result = new Player[playerNames.size()];
        for (int i = 0; i < playerNames.size(); i++){
                    result[i] = new HumanPlayer(playerNames.get(i));
        }
        return result;
    }

    public boolean isGamOver() {
        return gamOver;
    }
}
