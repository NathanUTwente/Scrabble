package NetworkScrabble.Network.ChatPackage;

import NetworkScrabble.Network.ProtocolMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements  Chat, Runnable{

    private Socket connection;      // For communication with the server.
    private BufferedReader in;  // Stream for receiving data from server.
    private PrintWriter out;     // Stream for sending data to server.
    Scanner userInput;

    public ChatClient(Socket connection){
        this.connection = connection;
        try {
            this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInput = new Scanner(System.in);
        System.out.println("here2");
    }

    @Override
    public void sendChat(String message) {

    }

    @Override
    public void receiveChat() {

    }

    @Override
    public void setUp(int port) {

    }

    @Override
    public void doHandshake() throws IOException {
        out.println(ProtocolMessages.HELLO);
        out.flush();

        String messageIn = in.readLine();
        if (!messageIn.equals(ProtocolMessages.WELCOME)){
            throw new IOException("Connected program is not Scrabble Chat!");
        } else {
            System.out.println("Connected with Scrabble Chat");
        }

    }

    @Override
    public void run() {

    }
}
