package Utils.Exceptions;

public class ConnectedWordDoesNotExistException extends InvalidMoveException{

    public ConnectedWordDoesNotExistException(String msg) {
        super(msg);
    }
}
