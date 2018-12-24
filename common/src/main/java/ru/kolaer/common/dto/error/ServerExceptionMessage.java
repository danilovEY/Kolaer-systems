package ru.kolaer.common.dto.error;

import lombok.Data;

import java.util.Date;

/**
 * Created by danilovey on 10.11.2016.
 */
@Data
public class ServerExceptionMessage {
    /**HTTP-статус*/
    private Integer status;
    /**Код ошибки на сервере*/
    private ErrorCode code;
    /**URL контроллера в котором произошла ошибка*/
    private String url;
    /**Сообщение для пользователей*/
    private String message;
    /**Системное сообщение об ошибке для разработчиков*/
    private String developerMessage;
    /**Передаваемый объект для разработчиков*/
    private Object developObject;
    /**Время ошибки*/
    private Date exceptionTimestamp;

    public ServerExceptionMessage(){}

    public ServerExceptionMessage(Integer status, String url, String message) {
        this(status, url, message, "", null, new Date());
    }

    public ServerExceptionMessage(Integer status, String url, String message, Object developObject) {
        this(status, url, message, "", developObject, new Date());
    }

    public ServerExceptionMessage(Integer status, String url, ErrorCode errorCode) {
        this(status, errorCode, url,
                errorCode.getMessage(), errorCode.getMessage(), null, new Date());
    }

    public ServerExceptionMessage(Integer status, String url, String message, ErrorCode errorCode) {
        this(status, errorCode, url,
                message, errorCode.getMessage(), null, new Date());
    }

    public ServerExceptionMessage(Integer status, String url, String message,
                                  String developerMessage, Object developObject, Date exceptionTimestamp) {
        this(status, ErrorCode.SERVER_ERROR, url,
                message, developerMessage, developObject, exceptionTimestamp);
    }

    public ServerExceptionMessage(Integer status, ErrorCode code, String url, String message,
                                  String developerMessage, Object developObject, Date exceptionTimestamp) {
        this.status = status;
        this.code = code;
        this.url = url;
        this.message = message;
        this.developerMessage = developerMessage;
        this.developObject = developObject;
        this.exceptionTimestamp = exceptionTimestamp;
    }

}
