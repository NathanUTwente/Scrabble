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
//        if (!move[0].equals(PASS)){
//            return move;
//        }
        return move;
    }

    public void myMoveConfirmed(int points, String[] move){
        game.updatePoints(myPlayer, points);
        myPlayer.removeTiles(getTilesToRemove(move));
        game.playMove(move);
        System.out.println("Somehow made it");
    }

    public void removeTiles(String tiles){

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

//    public static void main(String[] args) {
//        GameSlave gameMaster = new GameSlave();
//        gameMaster.setUpGame();
//    }
//
//    public GameSlave() {
//        this.tui = new TextBoardRepresentation();
//        this.moveChecker = new MoveChecker();
//    }
//
//    public void setUpGame(){
//        Player[] players = new Player[]{new HumanPlayer("Nathan"), new HumanPlayer("Lejla")};
//        setUpGame(players);
//    }
//
//    public void setUpGame(Player[] players){
//        game = new Game(players);
//        for (Player player : players){
//            player.giveTiles(game.getTileBag().getTilesForPlayer(player));
//        }
//        runGame();
//    }
//
//    public void runGame(){
//        while (!game.gameOver()){
//            Player currentPlayer = game.getNextPlayer();
//            tui.update(game.getBoard());
//            tui.updatePlayerDeck(currentPlayer);
//            String[] move = null;
//            boolean skip = false;
//            boolean validMove = false;
//            while (!validMove){
//                try {
//                    move = currentPlayer.determineMove(game.getBoard(), tui);
//                    if (move[0].equals("SKIP")){
//                        if (game.getTileBag().tilesLeftInBag() > 0) {
//                            swapTiles(currentPlayer, move[1]);
//                            skip = true;
//                        } else {
//                            throw new TileBagEmptyException("The tile bag is empty, you cannot swap tiles");
//                        }
//                    } else {
//                        moveChecker.checkMove(move, game.getBoard());
//                    }
//                    validMove = true;
//                } catch (InvalidMoveException e){
//                    System.out.println(e.getMessage());
//                }
//            }
//            if (!skip) {
//                currentPlayer.removeTiles(getTilesToRemove(move));
//                game.playMove(move);
//                game.updatePoints(currentPlayer, moveChecker.getLastMovePoints());
//            }
//            if (game.getTileBag().tilesLeftInBag() > 0) {
//                ArrayList<Tile> newTiles = game.getTileBag().getTilesForPlayer(currentPlayer);
//                currentPlayer.giveTiles(newTiles);
//            } else {
//                System.out.println("The tile bag is now empty, no more tile swaps are possible");
//            }
//            tui.displayScores(game.getScores());
//        }
//        tui.displayResults(game);
//        if (playAgain(game)){
//            System.out.println("Players voted to play again, good luck");
//            setUpGame(game.getPlayers());
//        } else {
//            System.out.println("One or more players decided not to play again");
//        }
//    }
//
//    public void swapTiles(Player player, String tilesToSwap){
//        String[] tilesToRemove = getTilesToRemove(new String[]{"", "", tilesToSwap});
//        player.removeTiles(tilesToRemove);
//    }
//
//
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
//
//    public boolean playAgain(Game game){
//        boolean result = true;
//        for (Player player : game.getPlayers()){
//            if (!tui.wantToPlayAgain(player)){
//                result = false;
//            }
//        }
//        return result;
//    }
}
