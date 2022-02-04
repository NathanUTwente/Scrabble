package NetworkScrabble.Network;

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
            waitForReady();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForReady() throws IOException {
            String messageIn = in.readLine();
            if (messageIn.equals(ProtocolMessages.SERVERREADY)) {
                System.out.println("You ready for a game?");
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    if (scanner.nextLine().equals("Y")) {
                        out.println(ProtocolMessages.CLIENTREADY);
                        out.flush();
                        System.out.println("Ready Sent");
                        break;
                    }
                }
            }
    }

//    public void waitForGame() throws IOException {
//        String messageIn = in.readLine();
//        if (){
//
//        }
//    }

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
        if (messageIn.split(ProtocolMessages.SEPARATOR)[0].equals(ProtocolMessages.HELLO)){
            if (messageIn.split(ProtocolMessages.SEPARATOR).length > 1){
                return messageIn.split(ProtocolMessages.SEPARATOR)[1].split(" ");
            }
        }
        return null;
    }
}
