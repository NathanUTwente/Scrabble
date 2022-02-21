package NetworkScrabble.Network;

import java.io.IOException;
import java.util.ArrayList;

public class LobbyHandler {


    ArrayList<ScrabbleClientHandler> clients = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    /**
     * Adds client to lobby
     * @param clientHandler client to add to lobby
     */
    public void addClientToLobby(ScrabbleClientHandler clientHandler){
        clients.add(clientHandler);
        names.add(clientHandler.getClientName());
    }


    /**
     * Tells each player who is in the lobby by giving it to each handler
     * Also asks each player if all players are listed in the lobby
     * @return true if all players are said to be present, false if not
     */
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
