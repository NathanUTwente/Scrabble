package NetworkScrabble.Network.ChatPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class ChatManager implements Runnable{

    ArrayList<ClientChatHandler> handlers = new ArrayList<>();
    ServerSocket listener;
    int players;
    int port;

    /**
     * Creates object and sets up port
     * @ensures port != null & players != null
     * @param port port to use for chat
     * @param players players in game
     */
    public ChatManager(int port, int players){
        this.port = port;
        this.players = players;
        setUp(port);
    }

    /**
     * Sets socket with given port
     * For each player in game it waits for them to connect and creates a handler for them on new thread
     * Also tells handler to do handshaker
     * @requires port != null && handlers.size() > 0
     * @param port port for listener
     */
    public void setUp(int port) {
        try {
            listener = new ServerSocket(port);
            for (int i = 0; i < players; i++) {
                Socket connection = listener.accept();
                ClientChatHandler newClient = new ClientChatHandler(connection);
                handlers.add(newClient);
                newClient.doHandshake();
                Thread clientThread = new Thread(newClient);
                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * While running checks each client's last received message every 1/2 second
     * Uses locks to fix bug
     * Sends out message to other client if found
     */
    @Override
    public void run() {
        while (true){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (ClientChatHandler clientChatHandler : handlers){
                Lock lock = new ReentrantLock();
                lock.lock();
                    String received = clientChatHandler.getReceived();
                    lock.unlock();
                    if (received != null) {
                        for (ClientChatHandler toSend : handlers) {
                            if (toSend != clientChatHandler) {
                                toSend.sendChat(received);
                            }
                        }
                    }
            }
        }

    }
}
