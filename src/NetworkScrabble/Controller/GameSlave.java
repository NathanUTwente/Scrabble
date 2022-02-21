package NetworkScrabble.Controller;

import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.Model.Game;
import NetworkScrabble.Model.PlayerModels.HumanPlayer;
import NetworkScrabble.Model.PlayerModels.Player;
import NetworkScrabble.Model.TileBag;
import NetworkScrabble.Utils.MoveChecker;
import NetworkScrabble.View.TextBoardRepresentation;

import java.util.ArrayList;

public class GameSlave {

    private Game game;
    private TextBoardRepresentation tui;
    private MoveChecker moveChecker;
    private Player myPlayer;
    private final static String PASS = "PASS";

    /**
     * Creates object
     * @ensures tui != null
     */
    public GameSlave() {
        tui = new TextBoardRepresentation();
    }

    public void setupGame(String[] playerNames, String myName){
        Player[]players = new Player[playerNames.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = new HumanPlayer(playerNames[i]);
            if (playerNames[i].equals(myName)){
                myPlayer = players[i];
            }
        }
        game = new Game(players);
    }

    /**
     * Adds given tiles to the players deck and tells the tui to display the deck
     * @param tiles to add to players deck
     */
    public void giveMeTiles(String[] tiles){
        ArrayList<Tile> tilesToGive = new ArrayList<>();
        for (String s : tiles){
            tilesToGive.add(new Tile(TileBag.stringToTile(s), TileBag.GetPointOfTile(s)));
        }
        myPlayer.giveTiles(tilesToGive);
        tui.updatePlayerDeck(myPlayer);
    }

    /**
     * Handles when it is this players turn
     * Asks tui to get move
     * @return returns move
     */
    public String[] myMove(){
        tui.update(game.getBoard());
        tui.updatePlayerDeck(myPlayer);
        String[] move = null;
        move = myPlayer.determineMove(game.getBoard(), tui);
        return move;
    }

    /**
     * When move confirmation is received from the server it updates the points with the given points and removes used tiles from the players tiledeck
     * Also places the confirmed move on the board
     * @param points to add
     * @param move to add to game
     */
    public void myMoveConfirmed(int points, String[] move){
        game.updatePoints(myPlayer, points);
        myPlayer.removeTiles(getTilesToRemove(move));
        game.playMove(move);
    }

    /**
     * Tells tui to display the games scores
     */
    public void displayScores(){
        tui.displayScores(game.getScores());
    }

    /**
     * Tells the player who the current player is, if its another players turn
     * @param name of other player
     */
    public void otherTurnInProgress(String name){
        System.out.println("Player " + name + " is currently playing, please wait");
    }

    /**
     * Takes the confirmed move of another person and adds it to the board and updates the points
     * @param move played by other player
     * @param points earned from the move
     * @param playerName who made the move
     */
    public void otherTurnDone(String[] move, int points, String playerName){
        game.playMove(move);
        Player currentPlayer = null;
        for (Player player : game.getPlayers()){
            if (player.getName().equals(playerName)){
                currentPlayer = player;
            }
        }
        game.updatePoints(currentPlayer, points);

    }

    /**
     * Takes a move and returns a string array of tiles to remove from the player
     * @param move
     * @return
     */
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
