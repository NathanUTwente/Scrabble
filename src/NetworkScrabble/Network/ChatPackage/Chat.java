package NetworkScrabble.Network.ChatPackage;

import java.io.IOException;

public interface Chat {
    public void sendChat(String message);
    public void doHandshake() throws IOException;
}
