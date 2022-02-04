package NetworkScrabble.Utils.Exceptions;

public class NotConnectedToWordException extends InvalidMoveException {

    public NotConnectedToWordException(String msg) {
        super(msg);
    }
}
