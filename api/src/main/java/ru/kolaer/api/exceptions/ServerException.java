package ru.kolaer.api.exceptions;

/**
 * Created by danilovey on 02.08.2016.
 *
 * Исключения при работе с серверами.
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
