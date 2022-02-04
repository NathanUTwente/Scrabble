package NetworkScrabble.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.in;

public class ScrabbleClient {

    static final int DEFAULT_PORT = 8028;
    private String name;
    private String[] playerNames;

    ScrabbleServerHandler serverHandler;
    Socket connection;


    public static void main(String[] args) {
        ScrabbleClient client = new ScrabbleClient();
        client.getName();
        client.connectToServer();
        client.waitForReady();
        client.getPlayerNames();
    }


    public void waitForReady(){
        try {
            serverHandler.waitForReady();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void waitForGame(){
//        try {
//
//        }
//    }

    public void getPlayerNames(){
        try {
            this.playerNames = serverHandler.getPlayers();
            System.out.println(playerNames);
            for (String name : playerNames){
                System.out.println(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getName(){
        System.out.println("Please enter your name");
        Scanner scanner = new Scanner(in);
            this.name =  scanner.nextLine();
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
            ScrabbleServerHandler handler = new ScrabbleServerHandler(connection, this.name);
            serverHandler = handler;
            handler.doHandShake();
            System.out.println("Connected.  Time for more");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
