package NetworkScrabble.Utils.Exceptions;

public class TileNotInDeckException extends InvalidMoveException {

    public TileNotInDeckException(String msg){
        super(msg);
    }
}
