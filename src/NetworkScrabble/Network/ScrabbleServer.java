package NetworkScrabble.Network;

import NetworkScrabble.Controller.GameMaster;
import NetworkScrabble.Model.BoardModel.Tile;
import NetworkScrabble.Model.PlayerModels.Player;
import NetworkScrabble.Utils.Exceptions.InvalidMoveException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static java.lang.System.in;

public class ScrabbleServer {

    static final int DEFAULT_PORT = 8028;

    ServerSocket listener;
    ArrayList<ScrabbleClientHandler> clients = new ArrayList<>();
    Socket connection;
    CountDownLatch countDownLatch;
    GameMaster gameMaster;
    HashMap<String, ScrabbleClientHandler> nameHandlers = new HashMap<>();
    HashMap<String, Player> namePlayers = new HashMap<>();
    int players;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);
        System.out.println("How many players in game?");
        int players = scanner.nextInt();
        scanner.close();
        ScrabbleServer scrabbleServer = new ScrabbleServer(players);
        scrabbleServer.setUpServer();
        scrabbleServer.setUpGame();
        scrabbleServer.runGame();
    }

    public ScrabbleServer(int players) {
        this.players = players;
    }

    public void runGame(){
//        while (!gameMaster.isGameOver()){
            runTurn();
            runTurn();
            runTurn();

//        }
    }

    public void runTurn(){
        Player currentPlayer = gameMaster.getCurrentPlayer();
        broadcastTurn(currentPlayer);
        System.out.println("To here");
        String[] move = getCurrentMove(currentPlayer);
        for (String m : move){
            System.out.println(m);
        }
        int processedMove = 0;
        try {
            processedMove = processMove(move);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
        System.out.println(processedMove);
        if (processedMove == -500){
            int earnedPoints = gameMaster.endOfMove(currentPlayer, move);
            sendEndOfTurn(move, earnedPoints, currentPlayer);
            //remove tiles played
            sendNewTiles(currentPlayer, move);
        } else {
            //player swapped
        }


    }

    public void sendEndOfTurn(String[] move, int earnedPoints, Player currentPlayer){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.sendMoveConfirm(move, earnedPoints, currentPlayer);
        }
    }

    public void broadcastTurn(Player player){
        for (ScrabbleClientHandler clientHandler : clients){
            clientHandler.broadcastTurn(player.getName());
        }
    }

    public String[] getCurrentMove(Player player){
        try {
            return nameHandlers.get(player.getName()).getTurnMove();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int processMove(String[] move) throws InvalidMoveException {
            if (move[0].equals("SKIP")) {
                //can throw tilebagemptyexception
                return gameMaster.swapTiles(move);
            } else {
                //can throw invalidmoveexception
                gameMaster.isMoveValid(move);
                return -500;
            }
    }


    public void setUpServer(){
        startScrabbleServer(players);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All clients connected, waiting for ready");
        clientsReady();
        for (ScrabbleClientHandler client : clients){
            System.out.println(client.getClientName());
            nameHandlers.put(client.getClientName(), client);
        }

    }

    public void setUpGame(){
        sendPlayerList();
        System.out.println("Sent Out Names");
        gameMaster = new GameMaster();
        ArrayList<String> names = new ArrayList<>(nameHandlers.keySet());
        Player[] players = gameMaster.makePlayers(names);
        gameMaster.setUpGame(players);
        for (Player player : players){
            namePlayers.put(player.getName(), player);
        }
        for (ScrabbleClientHandler handler : clients){
            Tile[] tiles = namePlayers.get(handler.getClientName()).getTileDeck();
            String[] tileStrings = new String[tiles.length];
            for (int i = 0; i < tiles.length; i++){
                tileStrings[i] = tiles[i].getTileLetter();
            }
            handler.sendTiles(tileStrings);
        }
        System.out.println(nameHandlers.keySet().equals(namePlayers.keySet()));
    }

    public void sendNewTiles(Player currentPlayer, String[] move){
        ArrayList<Tile> tiles = gameMaster.getNewTiles(currentPlayer, move);
        String[] tileStrings = new String[tiles.size()];
        for (int i = 0; i < tiles.size(); i++){
            tileStrings[i] = tiles.get(i).getTileLetter();
        }
        nameHandlers.get(currentPlayer.getName()).sendTiles(tileStrings);

    }


    public void startScrabbleServer(int players){
        int count = 0;
        try {
            listener = new ServerSocket(DEFAULT_PORT);
            System.out.println("Listening on port " + listener.getLocalPort());
            while (count < players) {
                connection = listener.accept();
                clients.add(new ScrabbleClientHandler(connection));
                count++;
            }
            listener.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int count2 = 0;
        countDownLatch = new CountDownLatch(players);
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
