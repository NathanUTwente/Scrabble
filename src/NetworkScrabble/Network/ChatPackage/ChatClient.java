package NetworkScrabble.Network.ChatPackage;

import NetworkScrabble.Network.ProtocolMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatClient implements  Chat, Runnable{

    private Socket connection;      // For communication with the server.
    private BufferedReader in;  // Stream for receiving data from server.
    private PrintWriter out;     // Stream for sending data to server.
    final Scanner userInput;
    private ChatClient listener;
    private boolean amListener;
    private String name;

    public ChatClient(Socket connection, boolean listener, String name){
        this.name = name;
        amListener = listener;
        this.connection = connection;
        try {
            this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInput = new Scanner(System.in);
        if (!amListener) {
            this.listener = new ChatClient(connection, true, name);
            Thread listenerThread = new Thread(this.listener);
            listenerThread.start();
        }
    }

    @Override
    public void sendChat(String message) {
        String messageOut = ProtocolMessages.CHAT_FLAG + ProtocolMessages.SEPARATOR + name + " : " + message;
        out.println(messageOut);
        out.flush();
        System.out.print("\b" + " - message sent");
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
        while (true){
            if (!amListener) {
                synchronized (userInput){
                if (userInput.hasNextLine()) {
                    String text = userInput.nextLine();
                    String[] split = text.split(" ");
                    if (split[0].toUpperCase(Locale.ROOT).equals("CHAT")) {
                        String message = text.substring(5);
                        sendChat(message);
                    }

                }
            }
            } else {
                try {
                        String messageIn = in.readLine();
                    String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
                    if (messageSplit[0].equals(ProtocolMessages.CHAT_FLAG)) {
                        System.out.println(messageSplit[1]);
                    }
            }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
