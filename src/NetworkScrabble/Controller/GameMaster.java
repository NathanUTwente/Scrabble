package NetworkScrabble.Controller;

import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.Model.Game;
import NetworkScrabble.Model.PlayerModels.HumanPlayer;
import NetworkScrabble.Model.PlayerModels.Player;
import NetworkScrabble.Model.TileBag;
import NetworkScrabble.Utils.Exceptions.InvalidMoveException;
import NetworkScrabble.Utils.Exceptions.InvalidNetworkMoveException;
import NetworkScrabble.Utils.Exceptions.TileBagEmptyException;
import NetworkScrabble.Utils.MoveChecker;
import NetworkScrabble.View.TextBoardRepresentation;

import javax.swing.*;
import java.util.ArrayList;

public class GameMaster {

    private Game game;
    private TextBoardRepresentation tui;
    private MoveChecker moveChecker;
    private boolean gameOver = false;

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

    public ArrayList<Tile> getNewTiles(Player currentPlayer, String[] move){
        if (game.getTileBag().tilesLeftInBag() > 0) {
            String[] toRemove = getTilesToRemove(move);
            for (Tile t : currentPlayer.getTileDeck()){
            }
            currentPlayer.removeTiles(toRemove);
            ArrayList<Tile> newTiles = game.getTileBag().getTilesForPlayer(currentPlayer);
            currentPlayer.giveTiles(newTiles);
            return newTiles;
    } else {
        System.out.println("The tile bag is now empty, no more tile swaps are possible");
    }
        return null;
    }


    public void isMoveValid(String[] move) throws InvalidNetworkMoveException {
        try {
            moveChecker.checkMove(move, game.getBoard());
        } catch (InvalidMoveException e) {
            throw new InvalidNetworkMoveException("Invalid Move, turn skipped");
        }
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

    public String giveMeATile(){
        return game.getTileBag().getTileOutOfBag().getTileLetter();
    }


    public void gameEnd(){
        tui.displayScores(game.getScores());
        tui.displayResults(game);

    }



    public String[] getTilesToRemove(String[] move){
        ArrayList<String> temp = new ArrayList<>();
        for (String l : move[2].split("")){
            if (!l.equals(".")){
                temp.add(l);
            }
        }
        String[] toRemoveFromPlayer = new String[temp.size()];
        for (int i = 0; i < toRemoveFromPlayer.length; i++){
            toRemoveFromPlayer[i] = temp.get(i);
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

    public boolean isGameOver() {
        boolean emptyDeck = false;
        for (Player player : game.getPlayers()){
            boolean empty = true;
            for (Tile t : player.getTileDeck()){
                if (t != null){
                    empty = false;
                }
            }
            if (empty){
                emptyDeck = true;
            }
        }
        return ((emptyDeck) && !(game.getTileBag().tilesLeftInBag() > 0));
    }
}
