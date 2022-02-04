package NetworkScrabble.Network;

import NetworkScrabble.Controller.GameMaster;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ScrabbleServer {

    static final int DEFAULT_PORT = 8028;

    ServerSocket listener;
    ArrayList<ScrabbleClientHandler> clients = new ArrayList<>();
    Socket connection;
    CountDownLatch countDownLatch;
    GameMaster gameMaster;

    public static void main(String[] args) {
        ScrabbleServer scrabbleServer = new ScrabbleServer();
        scrabbleServer.setUpServer();
    }


    public void setUpServer(){
        startScrabbleServer();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All clients connected, waiting for ready");
        clientsReady();
        for (ScrabbleClientHandler client : clients){
            System.out.println(client.getClientName());
        }

    }

    public void setUpGame(){
        sendPlayerList();
        System.out.println("Sent Out Names");


    }


    public void startScrabbleServer(){
        int count = 0;
        try {
            listener = new ServerSocket(DEFAULT_PORT);
            System.out.println("Listening on port " + listener.getLocalPort());
            while (count < 2) {
                connection = listener.accept();
                clients.add(new ScrabbleClientHandler(connection));
                count++;
            }
            listener.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int count2 = 0;
        countDownLatch = new CountDownLatch(count);
        for (ScrabbleClientHandler scrabbleHandler : clients) {
            Thread thread = new Thread(scrabbleHandler, "Thread " + count2);
            scrabbleHandler.setCountDownLatch(countDownLatch);
            thread.start();
        }

    }

    public boolean clientsReady(){
        boolean result = true;
        int count = 0;
        for (ScrabbleClientHandler client : clients){
            try {
                boolean response = client.getClientReady();
                if (!response){
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void sendPlayerList(){
        String[] playerList = new String[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            playerList[i] = clients.get(i).getClientName();
        }
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.sendPlayerList(playerList);
        }
    }


}
