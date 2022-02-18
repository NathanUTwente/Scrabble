package NetworkScrabble.Network.ChatPackage;

import NetworkScrabble.Network.ProtocolMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientChatHandler implements Runnable, Chat{


    private Socket connection;      // For communication with the client.
    private BufferedReader in;  // Stream for receiving data from client.
    private PrintWriter out;     // Stream for sending data to client.

    public ClientChatHandler(Socket connection){
        this.connection = connection;
        try {
            this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String messageIn = in.readLine();
        if ((messageIn.split(ProtocolMessages.SEPARATOR)[0].equals(ProtocolMessages.HELLO))){
            System.out.println("Connected with a ScrabbleClient");
            out.println(ProtocolMessages.WELCOME);
            out.flush();
        } else {
            throw new IOException("Connected program is not a ScrabbleClient!");
        }

    }

    @Override
    public void run() {

    }
}
