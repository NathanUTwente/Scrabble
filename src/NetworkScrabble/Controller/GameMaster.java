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

    /**
     * Creates object
     * @ensures tui != null & moveChecker != null
     */
    public GameMaster() {
        this.tui = new TextBoardRepresentation();
        this.moveChecker = new MoveChecker();
    }

    /**
     * Default game set up, for testing
     */
    public void setUpGame(){
        Player[] players = new Player[]{new HumanPlayer("Nathan"), new HumanPlayer("Lejla")};
        setUpGame(players);
    }

    /**
     * Setus up game with the given players and gives the players tiles
     * @param players array of players to set up game with
     */
    public void setUpGame(Player[] players){
        game = new Game(players);
        for (Player player : players){
            player.giveTiles(game.getTileBag().getTilesForPlayer(player));
        }
    }

    /**
     * Gets the next player to make a move and tells tui to print board
     * @return next player to move
     */
    public Player getCurrentPlayer(){
        Player currentPlayer = game.getNextPlayer();
        tui.update(game.getBoard());
        return currentPlayer;
    }

    /**
     * Adds the move to the game, adds the calculated points to the game
     * @param currentPlayer player who made the move
     * @param move move played
     * @return points
     */
    public int endOfMove(Player currentPlayer, String[] move){
        game.playMove(move);
        int lastPoints = moveChecker.getLastMovePoints();
        game.updatePoints(currentPlayer, lastPoints);
        return lastPoints;
    }

    /**
     * Takes in the move and counts tiles used in it and then gets new tiles of that number from the tilebag to give to the player
     * @param currentPlayer player who made move
     * @param move move made
     * @return array list of tiles to give
     */
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


    /**
     * Gives the move checker the current board to copy and the move to be played and asks if it is valid
     * @param move move to check
     * @throws InvalidNetworkMoveException if move is invalid
     */
    public void isMoveValid(String[] move) throws InvalidNetworkMoveException {
        try {
            moveChecker.checkMove(move, game.getBoard());
        } catch (InvalidMoveException e) {
            throw new InvalidNetworkMoveException("Invalid Move, turn skipped");
        }
    }

    /**
     * Adds the swapped tiles back to the tile bag and returns the number of tiles the player needs back
     * @param move tiles to swap
     * @return the number of new tiles the player needs
     * @throws TileBagEmptyException
     */
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

    /**
     * Gets a random tile from the bag
     * @return
     */
    public String giveMeATile(){
        return game.getTileBag().getTileOutOfBag().getTileLetter();
    }


    /**
     * Displays the scores and results at end of game
     */
    public void gameEnd(){
        tui.displayScores(game.getScores());
        tui.displayResults(game);

    }

    /**
     * Takes in a move and returns which tiles to remove from the players deck, filtering out '.'s
     * @param move the move given
     * @return string array of tiles to remove
     */
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

    /**
     * Asks if the player wants to play
     * @param game
     * @return whether the players want to play again
     */
    public boolean playAgain(Game game){
        boolean result = true;
        for (Player player : game.getPlayers()){
            if (!tui.wantToPlayAgain(player)){
                result = false;
            }
        }
        return result;
    }

    /**
     * Makes player objects from a list of names
     * @param playerNames names to make players for
     * @return array of players
     */
    public Player[] makePlayers(ArrayList<String> playerNames){
        Player[] result = new Player[playerNames.size()];
        for (int i = 0; i < playerNames.size(); i++){
                    result[i] = new HumanPlayer(playerNames.get(i));
        }
        return result;
    }

    /**
     * Checks all players to see if any of their tiledecks are empty and if so checks if the tile bag is also empty
     * @return true if both any player tile empty and tile bag is empty
     */
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
