package NetworkScrabble.Network;

import java.io.IOException;
import java.util.ArrayList;

public class LobbyHandler {


    ArrayList<ScrabbleClientHandler> clients = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();


    public void addClientToLobby(ScrabbleClientHandler clientHandler){
        clients.add(clientHandler);
        names.add(clientHandler.getClientName());
    }


    public boolean isAllPlayers() {
        String nameList = "";
        for (String name : names){
            nameList += name + " ";
        }
        boolean result = true;
        for (ScrabbleClientHandler handler : clients){
            try {
                if (!handler.allPlayersConnected(nameList)){
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
