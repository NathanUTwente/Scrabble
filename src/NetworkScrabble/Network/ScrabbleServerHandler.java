package NetworkScrabble.Network;

import NetworkScrabble.Utils.Exceptions.InvalidNetworkMoveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ScrabbleServerHandler implements Runnable{

    private Socket connection;      // For communication with the server.
    private BufferedReader in;  // Stream for receiving data from server.
    private PrintWriter out;     // Stream for sending data to server.

    private String name;

    Scanner userInput;

    /**
     * Creates the object and sets needed attributes
     * @param connection connection to communicate with client over
     * @param name name of player this client is responsible for
     * @requires connection != null && name != null
     * @ensures  this.connection != null && in != null && out != null && userInput != null && this.name != null
     * @throws IOException
     */
    public ScrabbleServerHandler(Socket connection, String name) throws IOException {
        this.connection = connection;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        userInput = new Scanner(System.in);
        this.name = name;
    }

    /**
     * Does network handshake, sends hello and players name to the server and waits for server response
     * Checks that response match preset
     * @throws IOException if connected to something other than Scrabble Server
     */
    public void doHandShake() throws IOException {
        out.println(ProtocolMessages.HELLO + ProtocolMessages.SEPARATOR + name);
        out.flush();

        String messageIn = in.readLine();
        if (!messageIn.equals(ProtocolMessages.WELCOME + ProtocolMessages.SEPARATOR + name)){
            throw new IOException("Connected program is not a ScrabbleServer!");
        } else {
            System.out.println("Connected with a ScrabbleServer");
        }
    }

    /**
     * Does the handshake and then waits to join a lobby
     */
    @Override
    public void run() {
        try {
            this.doHandShake();
            waitForLobby();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for server message, checks it equals preset and asks player if the players list from the server is complete
     * Sends back player response
     * If server message is a different preset it asks player if they are ready and sends confirmation when they are
     * @requires in != null && out != null
     * @return true if in game, false if just in lobby
     * @throws IOException
     */
    public boolean waitForLobby() throws IOException {
            String messageIn = in.readLine();
            String[] splitMessage = messageIn.split(ProtocolMessages.SEPARATOR);
            if (splitMessage.length > 1 && splitMessage[0].equals(ProtocolMessages.SERVERREADY)){
                System.out.println("You are currently in the lobby with:");
                for (String name : splitMessage[1].split(" ")){
                    System.out.println(name);
                }
                System.out.println("Is this all players?\nType \'Y\' for yes and \'N\' for no");
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.equals("Y")) {
                        out.println(ProtocolMessages.CLIENTREADY);
                        out.flush();
                        System.out.println("All Players Sent");
                        break;
                    } else if (nextLine.equals("N")){
                        out.println(ProtocolMessages.ERROR);
                        out.flush();
                        System.out.println("Not All Players Sent");
                        break;
                    } else {
                        System.out.println("Invalid response please try again");
                        break;
                    }
                }
                return false;

            } else if (messageIn.equals(ProtocolMessages.SERVERREADY)) {
                System.out.println("Are you ready for a game?\nType \'Y\' when ready");
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    if (scanner.nextLine().equals("Y")) {
                        out.println(ProtocolMessages.CLIENTREADY);
                        out.flush();
                        System.out.println("Ready Sent");
                        break;
                    }
                }
                return true;
            }
            return false;
    }

    /**
     * Waits to receive a list of tiles from server and returns them
     * @requires in != null
     * @return string array of tile letters received
     * @throws IOException
     */
    public String[] waitForTiles() throws IOException {
        String[] tiles;
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.TILES)){
            tiles = messageSplit[1].split(" ");
            return tiles;
        } else {
            throw new IOException("Where me Tiles");
        }

    }

    /**
     * Waits to receive the player list from the server and returns it
     * @return game player list (names) from server
     * @requires in != null
     * @throws IOException
     */
    public String[] getPlayers() throws IOException {
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.HELLO)){
            if (messageIn.split(ProtocolMessages.SEPARATOR).length > 1){
                return messageSplit[1].split(" ");
            }
        }
        return null;
    }

    /**
     * Waits to receive server broadcast of the current players turn
     * @requires in != null
     * @return name of current player or "PASS" if the player passed
     * @throws IOException
     */
    public String waitForTurnBroadcast() throws IOException {
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.TURN)){
            return messageSplit[1];
        } else if (messageSplit[0].equals(ProtocolMessages.PASS)){
            return "PASS";
        } else {
            return null;
        }
    }

    /**
     * Sends the move to the server with correct protocol
     * @requires out != null
     * @param move
     */
    public void sendMove(String[] move){
        String messageOut = ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR;
        for (String part : move){
            messageOut += part + " ";
        }
        out.println(messageOut);
        out.flush();

    }

    /**
     * Waits to receive move confirmation from the server and splits it into move format
     * If move was invalid an error message is received and the players turn is over
     * @requires in != null
     * @return move played
     * @throws IOException
     * @throws InvalidNetworkMoveException
     */
    public String[] waitForMoveConfirmation() throws IOException, InvalidNetworkMoveException {
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.MOVE)){
            String[] move = new String[3];
            for (int i = 1; i < messageSplit.length; i++){
                move[i - 1] = messageSplit[i];
            }
            return move;
        } else if ((messageSplit[0].equals(ProtocolMessages.ERROR)) && (messageSplit[1].equals(ProtocolMessages.INVALID_MOVE))){
            throw new InvalidNetworkMoveException("Your move was invalid, you dont get any points and you lose your turn");
        }
        return null;

    }

    /**
     * Sends move skip to the server with any tiles to swap in server protocol
     * @requires out != null
     * @param move
     */
    public void sendSkip(String[] move){
        String messageOut = ProtocolMessages.PASS;
        if (move[1].length() > 0){
            messageOut += ProtocolMessages.SEPARATOR;
            for (String s : move[1].split("")){
                messageOut += s + " ";
            }
        }
        out.println(messageOut);
        out.flush();
    }

    /**
     * Waits to receive the port to be used for the chat from the server
     * @requires in != null
     * @return port to be used
     * @throws IOException
     */
    public int waitForChat() throws IOException {
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.CHAT_FLAG)){
            return Integer.parseInt(messageSplit[1]);
        }
        return 0;
    }



}
