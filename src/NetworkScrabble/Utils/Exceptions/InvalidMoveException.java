package NetworkScrabble.Utils.Exceptions;

public abstract class InvalidMoveException extends Exception{

    public InvalidMoveException(String msg){
        super(msg);
    }
}
