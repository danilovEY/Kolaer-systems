package ru.kolaer.api.exeptions;

/**
 * Created by danilovey on 02.08.2016.
 */
public class ServerException extends RuntimeException  {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }
}
