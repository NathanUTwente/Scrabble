package NetworkScrabble.Network;

import NetworkScrabble.Controller.GameMaster;
import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.Model.PlayerModels.Player;
import NetworkScrabble.Network.ChatPackage.ChatManager;
import NetworkScrabble.Utils.Exceptions.InvalidMoveException;
import NetworkScrabble.Utils.Exceptions.InvalidNetworkMoveException;
import NetworkScrabble.Utils.Exceptions.TileBagEmptyException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ScrabbleServer {

    static final int DEFAULT_PORT = 8028;
    static final int DEFAULT_CHAT_PORT = 8029;

    ServerSocket listener;
    ArrayList<ScrabbleClientHandler> clients = new ArrayList<>();
    Socket connection;
    GameMaster gameMaster;
    HashMap<String, ScrabbleClientHandler> nameHandlers = new HashMap<>();
    HashMap<String, Player> namePlayers = new HashMap<>();
    LobbyHandler lobbyHandler;
    ChatManager chatManager;

    /**
     * Starts the server and tells it to do each step
     */
    public static void main(String[] args) {
        ScrabbleServer scrabbleServer = new ScrabbleServer();
        scrabbleServer.setUpServer();
        scrabbleServer.setUpChat(DEFAULT_CHAT_PORT);
        scrabbleServer.setUpGame();
        scrabbleServer.runGame();
    }

    /**
     * Tells clients to use a specified port for the chat and sets up the chat
     * Creates a handler on a new thread for each
     * @param port port that the chat should use to communicate
     */
    public void setUpChat(int port){
        for (ScrabbleClientHandler scrabbleClientHandler : clients){
            scrabbleClientHandler.sendChatInstructions(port);
        }
        chatManager = new ChatManager(port, clients.size());
        Thread chat = new Thread(chatManager);
        chat.start();
    }

    /**
     * Creates server object and creates lobby handler
     * @ensures lobbyHandler != null
     */
    public ScrabbleServer() {
        lobbyHandler = new LobbyHandler();
    }

    /**
     * Runs game until game is over
     */
    public void runGame(){
        while (!gameMaster.isGameOver()){
            runTurn();
        }
        gameMaster.gameEnd();
        broadcastGameOver();

    }

    /**
     * Sets up the server
     * Starts a server socket and listens for clients, creating a handler and a thread for each one, then putting them in the lobby
     * @ensures clients > 0 && nameHandlers > 0
     */
    public void setUpServer() {
        boolean ready = false;
        try {
            listener = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Listening on port " + listener.getLocalPort());
        int count = 0;
        while (!ready){
            try {
                connection = listener.accept();
                ScrabbleClientHandler newClient = new ScrabbleClientHandler(connection);
                clients.add(newClient);
                Thread thread = new Thread(newClient, "Thread " + count);
                count++;
                thread.start();

                System.out.println("Waiting");
                synchronized (newClient){
                    while (!newClient.wakeNeeded()){
                        newClient.wait();
                    }
                }
                lobbyHandler.addClientToLobby(newClient);
                System.out.println(newClient.getClientName());
                ready = lobbyHandler.isAllPlayers();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("All clients connected, waiting for ready");
        clientsReady();
        for (ScrabbleClientHandler client : clients){
            nameHandlers.put(client.getClientName(), client);
        }

    }

    /**
     * Asks each handler to send ready request to clients and returns result
     * @return true if all clients are ready, false if not
     */
    public boolean clientsReady(){
        boolean result = true;
        for (ScrabbleClientHandler client : clients){
            try {
                boolean response = client.getClientReady();
                if (!response){
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Sets up the game, tells handlers names of all players and to send them out
     * Creates a GameMaster and gives it a list of players to start a game and sends out the tile decks of each player to the client through the handlers
     * @ensures gameMaster != null
     */
    public void setUpGame(){
        sendPlayerList();
        System.out.println("Sent Out Names");
        gameMaster = new GameMaster();
        ArrayList<String> names = new ArrayList<>(nameHandlers.keySet());
        Player[] players = gameMaster.makePlayers(names);
        gameMaster.setUpGame(players);
        for (Player player : players){
            namePlayers.put(player.getName(), player);
        }
        for (ScrabbleClientHandler handler : clients){
            Tile[] tiles = namePlayers.get(handler.getClientName()).getTileDeck();
            String[] tileStrings = new String[tiles.length];
            for (int i = 0; i < tiles.length; i++){
                tileStrings[i] = tiles[i].getTileLetter();
            }
            handler.sendTiles(tileStrings);
        }
    }

    /**
     * Tells all clients to broadcast game over
     */
    public void broadcastGameOver(){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.broadcastGameOver();
        }
    }

    /**
     * Runs each turn
     * Tells handlers to broadcast turn
     * Gets the move of the player
     * Checks the move and calculates points
     * Adds this to the game
     * Sends the confirmation to players
     * Sends new tiles to player who played
     */
    public void runTurn(){
        Player currentPlayer = gameMaster.getCurrentPlayer();
        broadcastTurn(currentPlayer);
        String[] move = getCurrentMove(currentPlayer);
        int processedMove = 0;
        try {
            processedMove = processMove(move);
            if (processedMove == -500){
                int earnedPoints = gameMaster.endOfMove(currentPlayer, move);
                sendEndOfTurn(move, earnedPoints, currentPlayer);
                sendNewTiles(currentPlayer, move);
            } else {
                if (processedMove > 0){
                    sendNewTiles(currentPlayer, processedMove);
                }
                broadcastPass(currentPlayer);
            }
        } catch (InvalidMoveException e) {
            sendInvalidWord();
        }
    }

    /**
     * Broadcasts to all players that a player skipped their turn
     * @param player the player that skipped
     */
    public void broadcastPass(Player player){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.broadcastPass(player.getName());
        }
    }


    /**
     * Broadcasts to all players that the last move was invalid and the player lost their move
     */
    public void sendInvalidWord(){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.sendInvalidMove();
        }
    }

    /**
     * Sends to all players the last confirmed move
     * @param move the confirmed move
     * @param earnedPoints the points the move earned
     * @param currentPlayer the player who made the move
     */
    public void sendEndOfTurn(String[] move, int earnedPoints, Player currentPlayer){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.sendMoveConfirm(move, earnedPoints, currentPlayer);
        }
    }

    /**
     * Broadcasts to all players whose turn it is
     * @param player the current player
     */
    public void broadcastTurn(Player player){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.broadcastTurn(player.getName());
        }
    }

    /**
     * Asks the player for their move
     * @param player the player whose turn it is
     * @return the move the player sends back
     */
    public String[] getCurrentMove(Player player){
        try {
            return nameHandlers.get(player.getName()).getTurnMove();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks what type of move is played and whether it is valid
     * @param move the move
     * @return validity of move/type
     * @throws InvalidNetworkMoveException if the move is invalid
     * @throws TileBagEmptyException if the tiles cannot be swapped
     */
    public int processMove(String[] move) throws InvalidNetworkMoveException, TileBagEmptyException {
            if (move[0].equals("PASS")) {
                if (move.length == 2) {
                    return gameMaster.swapTiles(move);
                } else {
                    return 0;
                }
            } else {
                gameMaster.isMoveValid(move);
                return -500;
            }
    }

    /**
     *  Sends new tiles to the player who just made a move
     * @param currentPlayer
     * @param move
     */
    public void sendNewTiles(Player currentPlayer, String[] move){
        ArrayList<Tile> tiles = gameMaster.getNewTiles(currentPlayer, move);
        String[] tileStrings = new String[tiles.size()];
        for (int i = 0; i < tiles.size(); i++){
            tileStrings[i] = tiles.get(i).getTileLetter();
        }
        nameHandlers.get(currentPlayer.getName()).sendTiles(tileStrings);

    }

    /**
     * Sends a specified number of tiles to the specified player
     * @param currentPlayer
     * @param number
     */
    public void sendNewTiles(Player currentPlayer, int number){
        String[] tileStrings = new String[number];
        for (int i = 0; i < number; i++){
            tileStrings[i] = gameMaster.giveMeATile();
        }
        nameHandlers.get(currentPlayer.getName()).sendTiles(tileStrings);

    }

    /**
     * Broadcasts the player list ot each player
     */
    public void sendPlayerList(){
        String[] playerList = new String[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            playerList[i] = clients.get(i).getClientName();
        }
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.sendPlayerList(playerList);
        }
    }
}
