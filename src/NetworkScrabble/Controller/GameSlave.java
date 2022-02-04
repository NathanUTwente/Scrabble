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

    public void giveMeTiles(String[] tiles){
        ArrayList<Tile> tilesToGive = new ArrayList<>();
        for (String s : tiles){
            tilesToGive.add(new Tile(TileBag.stringToTile(s), TileBag.GetPointOfTile(s)));
        }
        myPlayer.giveTiles(tilesToGive);
        tui.updatePlayerDeck(myPlayer);
    }

    public String[] myMove(){
        tui.update(game.getBoard());
        tui.updatePlayerDeck(myPlayer);
        String[] move = null;
        move = myPlayer.determineMove(game.getBoard(), tui);
        return move;
    }

    public void myMoveConfirmed(int points, String[] move){
        game.updatePoints(myPlayer, points);
        myPlayer.removeTiles(getTilesToRemove(move));
        game.playMove(move);
    }

    public void displayScores(){
        tui.displayScores(game.getScores());
    }

    public void otherTurnInProgress(String name){
        System.out.println("Player " + name + " is currently playing, please wait");
    }

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
