package ru.kolaer.server.webportal.common.exception;

import org.springframework.http.HttpStatus;
import ru.kolaer.common.mvp.model.error.ErrorCode;

/**
 * Ошибка при получении неправильных данных от клиента.
 *
 * Created by Danilov on 27.04.2017.
 */
public class CustomHttpCodeException extends ServerException {

    private HttpStatus httpStatus;

    public CustomHttpCodeException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, errorCode);
        this.httpStatus = httpStatus;
    }

    public CustomHttpCodeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus.getReasonPhrase());
        this.httpStatus = httpStatus;
    }

    public CustomHttpCodeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CustomHttpCodeException(String message, ErrorCode code, Object developObject) {
        super(message, code, developObject);
    }

    public CustomHttpCodeException(String message, ErrorCode code, String developMessage) {
        super(message, code, developMessage);
    }

    public CustomHttpCodeException(String message, Throwable cause, ErrorCode code) {
        super(message, cause, code);
    }

    public CustomHttpCodeException(String message, Throwable cause, ErrorCode code, String developMessage) {
        super(message, cause, code, developMessage);
    }

    public CustomHttpCodeException(Throwable cause, ErrorCode code) {
        super(cause, code);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
