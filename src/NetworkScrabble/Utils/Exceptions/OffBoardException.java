package NetworkScrabble.Utils.Exceptions;

public class OffBoardException extends InvalidMoveException {
    public OffBoardException(String msg) {
        super(msg);
    }
}
