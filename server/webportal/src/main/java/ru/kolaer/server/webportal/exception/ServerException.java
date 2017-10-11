package ru.kolaer.server.webportal.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.api.mvp.model.error.ErrorCode;

/**
 * Created by danilovey on 11.10.2017.
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ServerException extends RuntimeException {
    /**
     * Код ошибки на сервере
     */
    protected ErrorCode code;
    /**
     * Сообщение для разработчиков
     */
    protected String developMessage;
    /**
     * Передаваемый объект для разработчиков
     */
    protected Object developObject;

    public ServerException(String message, Object developObject) {
        super(message);
        this.developObject = developObject;
    }

    public ServerException(ErrorCode errorCode, String developMessage) {
        this(errorCode.getMessage(), errorCode, developMessage);
    }

    public ServerException(String developMessage) {
        this(ErrorCode.SERVER_ERROR.getMessage(), ErrorCode.SERVER_ERROR, developMessage);
    }

    public ServerException(String message, ErrorCode errorCode) {
        this(message, errorCode, errorCode.getMessage());
    }

    public ServerException(String message, ErrorCode errorCode, Object developObject) {
        this(message, errorCode, errorCode.getMessage());
        this.developObject = developObject;
    }

    public ServerException(String message, ErrorCode code, String developMessage) {
        super(message);
        this.code = code;
        this.developMessage = developMessage;
    }

    public ServerException(String message, ErrorCode code, String developMessage, Object developObject) {
        super(message);
        this.code = code;
        this.developMessage = developMessage;
        this.developObject = developObject;
    }

    public ServerException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public ServerException(String message, Throwable cause,
                           ErrorCode code, String developMessage) {
        super(message, cause);
        this.code = code;
        this.developMessage = developMessage;
    }

    public ServerException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;
    }

    public ServerException(String message, Throwable cause,
                               ErrorCode errorCode, Object developObject) {
        this(message, cause, errorCode, errorCode.getMessage());
        this.developObject = developObject;
    }
}
