package NetworkScrabble.Utils.Exceptions;

import java.io.IOException;

public class InvalidAnswerException extends IOException {
    public InvalidAnswerException(String message) {
        super(message);
    }
}
