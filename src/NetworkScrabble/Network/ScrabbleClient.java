package NetworkScrabble.Network;

import java.io.IOException;
import java.net.Socket;

public class ScrabbleClient {

    static final int DEFAULT_PORT = 8028;

    ScrabbleServerHandler serverHandler;
    Socket connection;

    public static void main(String[] args) {
        ScrabbleClient client = new ScrabbleClient();
        client.connectToServer();
        client.waitForGame();
    }

    public void waitForGame(){
        try {
            serverHandler.waitForGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void connectToServer(){

        try {
            System.out.println("Connecting on port " + DEFAULT_PORT);
            connection = new Socket("", DEFAULT_PORT);
        } catch (IOException e) {
            System.out.println("An error occurred while opening connection.");
            System.out.println(e);
        }

        try {
            ScrabbleServerHandler handler = new ScrabbleServerHandler(connection);
            serverHandler = handler;
            handler.doHandShake();
            System.out.println("Connected.  Time for more");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
