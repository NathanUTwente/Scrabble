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
    private boolean inGame = false;

    Scanner userInput;

    public ScrabbleServerHandler(Socket connection) throws IOException {
        this.connection = connection;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        userInput = new Scanner(System.in);
    }

    public void doHandShake() throws IOException {
        out.println(ProtocolMessages.HELLO);
        out.flush();

        String messageIn = in.readLine();
        if (!messageIn.equals(ProtocolMessages.WELCOME)){
            throw new IOException("Connected program is not a ScrabbleServer!");
        } else {
            System.out.println("Connected with a ScrabbleServer");
        }
    }

    @Override
    public void run() {
        try {
            this.doHandShake();
            waitForGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForGame() throws IOException {
        while (!inGame) {
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
    }
}
