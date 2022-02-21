package NetworkScrabble.Network;

import NetworkScrabble.Controller.GameSlave;
import NetworkScrabble.Network.ChatPackage.ChatClient;
import NetworkScrabble.Utils.Exceptions.InvalidNetworkMoveException;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.in;

public class ScrabbleClient {

    static final int DEFAULT_PORT = 8028;
    private String name;
    private String[] playerNames;

    ScrabbleServerHandler serverHandler;
    Socket connection;
    GameSlave gameSlave;
    boolean gameOver = false;
    private ChatClient chatClient;

    /**
     * Starts the client.
     * Gets clients name, connects to server, lobby and game, and then controls client playing game
     */
    public static void main(String[] args) {
        ScrabbleClient client = new ScrabbleClient();
        client.getName();
        client.connectToServer();
        client.waitForLobbyAndReady();
        client.setUpChat();
        client.getPlayerNames();
        client.setUpGame();
        client.play();
    }

    /**
     * Sets up the chat for the client
     * @ensures client is connected to chat
     */
    public void setUpChat() {
        try {
            System.out.println("waiting");
            int port = serverHandler.waitForChat();
            System.out.println(port);
            chatClient = new ChatClient(new Socket("", port), false, name);
            chatClient.doHandshake();
            Thread chatThread = new Thread(chatClient);
            chatThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instructs the handler to wait for tiles and gives tiles to the GameSlave to give the player
     * @requires serverHandler != null && gameSlave != null
     */
    public void waitForTiles(){
        try {
            String[] tiles = serverHandler.waitForTiles();
            gameSlave.giveMeTiles(tiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Instructs the handler to wait for lobby
     * @requires serverHandler != null
     */
    public void waitForLobbyAndReady(){
        boolean ready = false;
        while (!ready) {
            try {
                ready = serverHandler.waitForLobby();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the game on the client side, and then waits to receive starting tiles
     */
    public void setUpGame(){
        gameSlave = new GameSlave();
        gameSlave.setupGame(playerNames, name);
        waitForTiles();
    }

    /**
     * Instructs the handler to get the names of all players in game
     * @requires serverHandler != null
     */
    public void getPlayerNames(){
        try {
            this.playerNames = serverHandler.getPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the users name
     * @ensures name != null
     */
    public void getName(){
        System.out.println("Please enter your name");
        Scanner scanner = new Scanner(in);
        this.name =  scanner.nextLine();
    }


    /**
     * Sets up connection to server and does handshake
     * @ensures serverHandler != null
     */
    public void connectToServer(){

        try {
            System.out.println("Connecting on port " + DEFAULT_PORT);
            connection = new Socket("", DEFAULT_PORT);
        } catch (IOException e) {
            System.out.println("An error occurred while opening connection.");
            System.out.println(e);
        }

        try {
            ScrabbleServerHandler handler = new ScrabbleServerHandler(connection, this.name);
            serverHandler = handler;
            handler.doHandShake();
            System.out.println("Connected.  Welcome to the lobby, please wait for others to join");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Plays the game for the client, asks handler wait turn it is and tells the handler to tell the server its moves
     * @requires serverHandler != null && gameSlave != null
     */
    public void play(){
        while (!gameOver) {
            boolean pass = false;
            try {
                String turn = serverHandler.waitForTurnBroadcast();
                if (turn.equals(name)) {
                    String[] move = gameSlave.myMove();
                    if (move[0].equals("PASS")) {
                        serverHandler.sendSkip(move);
                        if (move[1].length() > 0) {
                            String[] tileString = serverHandler.waitForTiles();
                            gameSlave.giveMeTiles(tileString);
                            continue;
                        }

                    } else {
                        serverHandler.sendMove(move);
                    }

                } else if (turn.equals("PASS")) {
                    System.out.println("Player skipped their turn");
                    pass = true;
                    continue;
                } else {
                    gameSlave.otherTurnInProgress(turn);
                }
                String[] confirmedMove = new String[0];
                if (!pass){
                try {
                    confirmedMove = waitForMoveConfirmation();
                    if (confirmedMove[0].equals(name)) {
                        String[] move = confirmedMove[1].split(" ");
                        gameSlave.myMoveConfirmed((int) Integer.parseInt(confirmedMove[2]), move);
                        gameSlave.displayScores();
                        waitForTiles();
                    } else {
                        gameSlave.otherTurnDone(confirmedMove[1].split(" "), (int) Integer.parseInt(confirmedMove[2]), confirmedMove[0]);
                        gameSlave.displayScores();
                    }
                } catch (InvalidNetworkMoveException e) {
                    if (turn.equals(name)) {
                        System.out.println(e.getMessage());
                    } else {
                        System.out.println("Player " + turn + " was skipped as they played an invalid word");
                    }
                }
            }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Waits for a move confirmation from server, this player's or another's
     * @return move that is confirmed
     * @throws IOException
     * @throws InvalidNetworkMoveException
     */
    public String[] waitForMoveConfirmation() throws IOException, InvalidNetworkMoveException {
        return serverHandler.waitForMoveConfirmation();
    }


}
