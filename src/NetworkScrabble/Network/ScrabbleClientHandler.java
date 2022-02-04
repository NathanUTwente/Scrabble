package NetworkScrabble.Network;

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

    Scanner userInput;

    public ScrabbleClientHandler(Socket connection) throws IOException {
        this.connection = connection;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        userInput = new Scanner(System.in);
    }

    public void doHandShake() throws IOException {
        out.println(ProtocolMessages.WELCOME);
        out.flush();

        String messageIn = in.readLine();
        if (!messageIn.equals(ProtocolMessages.HELLO)){
            throw new IOException("Connected program is not a ScrabbleClient!");
        } else {
            System.out.println("Connected with a ScrabbleClient");
        }
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            this.doHandShake();
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getClientReady() throws IOException {
        out.println(ProtocolMessages.SERVERREADY);
        out.flush();
        String messageIn;
        messageIn = in.readLine();
        return messageIn.equals(ProtocolMessages.CLIENTREADY);
    }
}
