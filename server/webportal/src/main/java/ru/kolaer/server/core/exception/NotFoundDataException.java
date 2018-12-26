package ru.kolaer.server.core.exception;

import ru.kolaer.common.dto.error.ErrorCode;

/**
 * Created by danilovey on 11.10.2017.
 */
public class NotFoundDataException extends ServerException {

    public NotFoundDataException() {
        super("Не найдено", ErrorCode.OBJECT_NOT_FOUND);
    }

    public NotFoundDataException(String message) {
        super(message, ErrorCode.OBJECT_NOT_FOUND);
    }

    public NotFoundDataException(String message, Object developObject) {
        super(message, ErrorCode.OBJECT_NOT_FOUND, developObject);
    }

    public NotFoundDataException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NotFoundDataException(String message, ErrorCode code, Object developObject) {
        super(message, code, developObject);
    }

    public NotFoundDataException(String message, ErrorCode code, String developMessage) {
        super(message, code, developMessage);
    }

    public NotFoundDataException(String message, Throwable cause, ErrorCode code) {
        super(message, cause, code);
    }

    public NotFoundDataException(String message, Throwable cause, ErrorCode code, String developMessage) {
        super(message, cause, code, developMessage);
    }

    public NotFoundDataException(Throwable cause, ErrorCode code) {
        super(cause, code);
    }

}
