package NetworkScrabble.Network;

import NetworkScrabble.Model.PlayerModels.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class ScrabbleClientHandler implements Runnable{

    private Socket connection;      // For communication with the client.
    private BufferedReader in;  // Stream for receiving data from client.
    private PrintWriter out;     // Stream for sending data to client.
    private CountDownLatch countDownLatch;
    private String clientName;
    private boolean wake;

    Scanner userInput;

    /**
     * Sets up the handler on the given socket
     * @param connection
     * @throws IOException
     */
    public ScrabbleClientHandler(Socket connection) throws IOException {
        this.connection = connection;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        userInput = new Scanner(System.in);
    }

    /**
     * Waits to here from the client then sends a handshake message
     * @throws IOException
     */
    public void doHandShake() throws IOException {
        String messageIn = in.readLine();
        if ((messageIn.split(ProtocolMessages.SEPARATOR)[0].equals(ProtocolMessages.HELLO)) ){
            System.out.println("Connected with a ScrabbleClient");
            this.clientName = messageIn.split(ProtocolMessages.SEPARATOR)[1];
            out.println(ProtocolMessages.WELCOME + ProtocolMessages.SEPARATOR + clientName);
            out.flush();
        } else {
            throw new IOException("Connected program is not a ScrabbleClient!");
        }

    }

    /**
     * Does the handshake and then notifies the server
     */
    @Override
    public void run() {
        synchronized (this) {
            wake = false;
            try {
                this.doHandShake();
                wake = true;
                this.notifyAll();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends out a ready message with the list of players connected and waits for a confirmation that all players are connected
     * @param nameList
     * @return whether the client says all players are connected
     * @throws IOException
     */
    public boolean allPlayersConnected(String nameList) throws IOException {
        out.println(ProtocolMessages.SERVERREADY + ProtocolMessages.SEPARATOR + nameList);
        out.flush();
        String messageIn;
        messageIn = in.readLine();
        return messageIn.equals(ProtocolMessages.CLIENTREADY);
    }

    /**
     * Asks client if ready to play
     * @return true if client is ready
     * @throws IOException
     */
    public boolean getClientReady() throws IOException {
        out.println(ProtocolMessages.SERVERREADY);
        out.flush();
        String messageIn;
        messageIn = in.readLine();
        return messageIn.equals(ProtocolMessages.CLIENTREADY);
    }


    /**
     * Returns the name of the client this handler is responsible for
     * @return the name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Sends the list of players to the client at the start of the game
     * @param playerNames
     */
    public void sendPlayerList(String[] playerNames){
        String messageOut = ProtocolMessages.HELLO + ProtocolMessages.SEPARATOR;
        for (String name : playerNames){
            messageOut += name + " ";
        }
        out.println(messageOut);
        out.flush();
    }

    /**
     * Sends the given tiles to the player
     * @param tiles tiles to give
     */
    public void sendTiles(String[] tiles){
        String messageOut = ProtocolMessages.TILES + ProtocolMessages.SEPARATOR;
        for (String tile : tiles){
            messageOut += tile + " ";
        }
        out.println(messageOut);
        out.flush();
    }

    /**
     * Sends the name of the current players turn
     * @param name of player whose turn it is
     */
    public void broadcastTurn(String name){
        String messageOut = ProtocolMessages.TURN + ProtocolMessages.SEPARATOR;
        messageOut += name;
        out.println(messageOut);
        out.flush();
    }

    /**
     * Sends the name of the player who passed their turn
     * @param name of the player who passed
     */
    public void broadcastPass(String name){
        String messageOut = ProtocolMessages.PASS + ProtocolMessages.SEPARATOR;
        messageOut += name;
        out.println(messageOut);
        out.flush();
    }

    /**
     * Gets the move the player sends to the server
     * @return the move of the player
     * @throws IOException
     */
    public String[] getTurnMove() throws IOException {
        String[] move = new String[3];
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.MOVE)){
            int i = 0;
            for (String part : messageSplit[1].split(" ")){
                move[i] = part;
                i++;
            }
        } else if (messageSplit[0].equals(ProtocolMessages.PASS)){
            return messageSplit;
        }
        return move;
    }

    /**
     * Sends out a confirmed move to the player
     * @param move the move confirmed
     * @param earnedPoints the points to move earned
     * @param currentPlayer the player who made the move
     */
    public void sendMoveConfirm(String[] move, int earnedPoints, Player currentPlayer){
        String messageOut = ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR + currentPlayer.getName() + ProtocolMessages.SEPARATOR;
        for (String part : move){
            messageOut +=part + " ";
        }
        messageOut += ProtocolMessages.SEPARATOR + earnedPoints;
        out.println(messageOut);
        out.flush();
    }

    /**
     * Sends out the invalid move message
     */
    public void sendInvalidMove(){
        String messageOut = ProtocolMessages.ERROR+ ProtocolMessages.SEPARATOR + ProtocolMessages.INVALID_MOVE;
        out.println(messageOut);
        out.flush();
    }

    /**
     * Sends out that the game os over
     */
    public void broadcastGameOver(){
        String messageOut = ProtocolMessages.GAMEOVER;
        out.println(messageOut);
        out.flush();
    }

    /**
     * Returns whether wake is needed
     * @return boolean if wake is needed
     */
    public boolean wakeNeeded() {
        return wake;
    }

    /**
     * Sends out the port to be used by the chat
     * @param port
     */
    public void sendChatInstructions(int port){
        String messageOut = ProtocolMessages.CHAT_FLAG + ProtocolMessages.SEPARATOR + port;
        out.println(messageOut);
        out.flush();
    }
}
