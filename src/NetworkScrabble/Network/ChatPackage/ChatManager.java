package NetworkScrabble.Network.ChatPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatManager {

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
                System.out.println("here");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
