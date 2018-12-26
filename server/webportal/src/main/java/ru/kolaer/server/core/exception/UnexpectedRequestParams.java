package ru.kolaer.server.core.exception;

import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.common.dto.error.UnexpectedParamsDescription;

/**
 * Ошибка при получении неправильных данных от клиента.
 *
 * Created by Danilov on 27.04.2017.
 */
public class UnexpectedRequestParams extends ServerException {

    public UnexpectedRequestParams(UnexpectedParamsDescription... unexpectedParamsDescriptions) {
        super(ErrorCode.INCORRECT_REQUEST_VALUE.getMessage(), ErrorCode.INCORRECT_REQUEST_VALUE, unexpectedParamsDescriptions);
    }

    public UnexpectedRequestParams(String message) {
        super(message, ErrorCode.INCORRECT_REQUEST_VALUE);
    }

    public UnexpectedRequestParams(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UnexpectedRequestParams(String message, Object developObject) {
        super(message, ErrorCode.INCORRECT_REQUEST_VALUE, developObject);
    }

    public UnexpectedRequestParams(String message, ErrorCode code, Object developObject) {
        super(message, code, developObject);
    }

    public UnexpectedRequestParams(String message, ErrorCode code, String developMessage) {
        super(message, code, developMessage);
    }

    public UnexpectedRequestParams(String message, Throwable cause, ErrorCode code) {
        super(message, cause, code);
    }

    public UnexpectedRequestParams(String message, Throwable cause, ErrorCode code, String developMessage) {
        super(message, cause, code, developMessage);
    }

    public UnexpectedRequestParams(String message, ErrorCode code, String developMessage, Object developObject) {
        super(message, code, developMessage, developObject);
    }

    public UnexpectedRequestParams(Throwable cause, ErrorCode code) {
        super(cause, code);
    }

}
