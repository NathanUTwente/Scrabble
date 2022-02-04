package NetworkScrabble.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ScrabbleServer {

    static final int DEFAULT_PORT = 8028;

    ServerSocket listener;
    ArrayList<ScrabbleHandler> scrabbleHandlers = new ArrayList<>();
    Socket connection;

    public static void main(String[] args) {
        ScrabbleServer scrabbleServer = new ScrabbleServer();
        scrabbleServer.startScrabbleServer();
    }


    public void startScrabbleServer(){
        try {
            int count = 0;
            while (count < 2) {
                listener = new ServerSocket(DEFAULT_PORT);
                System.out.println("Listening on port " + listener.getLocalPort());
                connection = listener.accept();
                scrabbleHandlers.add(new ScrabbleHandler(connection));
                listener.close();
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            for (ScrabbleHandler scrabbleHandler : scrabbleHandlers) {
                scrabbleHandler.doHandShake();
                System.out.println("Handshake done successfully,  ready for more shit");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
