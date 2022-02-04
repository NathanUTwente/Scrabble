package NetworkScrabble.Network;

import NetworkScrabble.Controller.GameSlave;

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
    GameSlave gameSlave;
    boolean gameOver = false;


    public static void main(String[] args) {
        ScrabbleClient client = new ScrabbleClient();
        client.getName();
        client.connectToServer();
        client.waitForReady();
        client.getPlayerNames();
        client.setUpGame();
        client.play();
    }

    public void waitForTiles(){
        try {
            String[] tiles = serverHandler.waitForTiles();
            gameSlave.giveMeTiles(tiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void waitForReady(){
        try {
            serverHandler.waitForReady();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUpGame(){
        gameSlave = new GameSlave();
        gameSlave.setupGame(playerNames, name);
        waitForTiles();
    }

    public void getPlayerNames(){
        try {
            this.playerNames = serverHandler.getPlayers();
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

    public void play(){
        while (!gameOver){
            try {
                String turn = serverHandler.waitForTurnBroadcast();
                if (turn.equals(name)){
                    String[] move = gameSlave.myMove();
                    serverHandler.sendMove(move);

                } else {
                    //other player move
                }
                String[] confirmedMove = waitForMoveConfirmation();
                for (String part : confirmedMove){
                    System.out.println(part);
                }
                if (confirmedMove[0].equals(name)){
                    String[] move = confirmedMove[1].split(" ");
                    gameSlave.myMoveConfirmed((int) Integer.parseInt(confirmedMove[2]), move);
                }
                waitForTiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] waitForMoveConfirmation() throws IOException {
        return serverHandler.waitForMoveConfirmation();
    }


}
