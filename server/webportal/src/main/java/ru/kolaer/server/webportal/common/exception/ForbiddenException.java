package ru.kolaer.server.webportal.common.exception;

import ru.kolaer.common.mvp.model.error.ErrorCode;

public class ForbiddenException extends ServerException {

    public ForbiddenException() {
        this(ErrorCode.FORBIDDEN.getMessage(), ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(String developMessage) {
        this(developMessage, ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
