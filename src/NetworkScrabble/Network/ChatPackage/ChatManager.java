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

    public ChatManager(int port, int players){
        this.port = port;
        this.players = players;
        setUp(port);
    }

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
