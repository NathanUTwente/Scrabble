package NetworkScrabble.Network.ChatPackage;

import NetworkScrabble.Network.ProtocolMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientChatHandler implements Runnable, Chat{


    private Socket connection;      // For communication with the client.
    private BufferedReader in;  // Stream for receiving data from client.
    private PrintWriter out;     // Stream for sending data to client.
    private String received = null;

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
        String messageOut = ProtocolMessages.CHAT_FLAG + ProtocolMessages.SEPARATOR + message;
        out.println(messageOut);
        out.flush();

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
        while (true){
            try {
                String messageIn = in.readLine();
                received = messageIn.split(ProtocolMessages.SEPARATOR)[1];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getReceived() {
        Lock lock = new ReentrantLock();
        lock.lock();
        String result = received;
        received = null;
        lock.unlock();
        return result;
    }
}
