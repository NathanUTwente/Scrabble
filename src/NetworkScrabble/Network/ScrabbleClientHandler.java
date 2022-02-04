package NetworkScrabble.Network;

import NetworkScrabble.Model.PlayerModels.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class ScrabbleClientHandler implements Runnable{

    private Socket connection;      // For communication with the client.
    private BufferedReader in;  // Stream for receiving data from client.
    private PrintWriter out;     // Stream for sending data to client.
    private CountDownLatch countDownLatch;
    private String clientName;

    Scanner userInput;

    public ScrabbleClientHandler(Socket connection) throws IOException {
        this.connection = connection;
        this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream());
        userInput = new Scanner(System.in);
    }

    public void doHandShake() throws IOException {
        String messageIn = in.readLine();
        if ((messageIn.split(ProtocolMessages.SEPARATOR)[0].equals(ProtocolMessages.HELLO)) ){
            System.out.println("Connected with a ScrabbleClient");
            this.clientName = messageIn.split(ProtocolMessages.SEPARATOR)[1];
            out.println(ProtocolMessages.WELCOME + ProtocolMessages.SEPARATOR + clientName);
            out.flush();
        } else {
            throw new IOException("Connected program is not a ScrabbleClient!");
        }

    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            this.doHandShake();
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getClientReady() throws IOException {
        out.println(ProtocolMessages.SERVERREADY);
        out.flush();
        String messageIn;
        messageIn = in.readLine();
        return messageIn.equals(ProtocolMessages.CLIENTREADY);
    }

    public String getClientName() {
        return clientName;
    }

    public void sendPlayerList(String[] playerNames){
        String messageOut = ProtocolMessages.HELLO + ProtocolMessages.SEPARATOR;
        for (String name : playerNames){
            messageOut += name + " ";
        }
        out.println(messageOut);
        out.flush();
    }

    public void sendTiles(String[] tiles){
        String messageOut = ProtocolMessages.TILES + ProtocolMessages.SEPARATOR;
        for (String tile : tiles){
            messageOut += tile + " ";
        }
        out.println(messageOut);
        out.flush();
    }

    public void broadcastTurn(String name){
        String messageOut = ProtocolMessages.TURN + ProtocolMessages.SEPARATOR;
        messageOut += name;
        out.println(messageOut);
        out.flush();
    }

    public String[] getTurnMove() throws IOException {
        String[] move = new String[3];
        String messageIn = in.readLine();
        System.out.println("Heard");
        String[] messageSplit = messageIn.split(ProtocolMessages.SEPARATOR);
        if (messageSplit[0].equals(ProtocolMessages.MOVE)){
            int i = 0;
            for (String part : messageSplit[1].split(" ")){
                move[i] = part;
                i++;
            }
        }
        return move;
    }

    public void sendMoveConfirm(String[] move, int earnedPoints, Player currentPlayer){
        String messageOut = ProtocolMessages.MOVE + ProtocolMessages.SEPARATOR + currentPlayer.getName() + ProtocolMessages.SEPARATOR;
        for (String part : move){
            messageOut +=part + " ";
        }
        messageOut += ProtocolMessages.SEPARATOR + earnedPoints;
        out.println(messageOut);
        out.flush();
    }

    public void sendInvalidMove(){
        String messageOut = ProtocolMessages.ERROR+ ProtocolMessages.INVALID_MOVE;
        out.println(messageOut);
        out.flush();
    }
}
