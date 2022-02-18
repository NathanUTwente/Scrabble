package NetworkScrabble.Network;

import NetworkScrabble.Utils.Exceptions.InvalidNetworkMoveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ScrabbleServerHandler implements Runnable{

    private Socket connection;      // For communication with the client.
    private BufferedReader in;  // Stream for receiving data from client.
    private PrintWriter out;     // Stream for sending data to client.
//    private boolean ready = false;

    private String name;

    Scanner userInput;

    public ScrabbleServerHandler(Socket connection, String name) throws IOException {
        this.connection = connection;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        userInput = new Scanner(System.in);
        this.name = name;
    }

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

    @Override
    public void run() {
        try {
            this.doHandShake();
            waitForLobby();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
//                scanner.close();
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

    public void sendMove(String[] move){
        String messageOut = ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR;
        for (String part : move){
            messageOut += part + " ";
        }
        out.println(messageOut);
        out.flush();

    }

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

    public int waitForChat() throws IOException {
        String messageIn = in.readLine();
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.CHAT_FLAG)){
            return Integer.parseInt(messageSplit[1]);
        }
        return 0;
    }



}
