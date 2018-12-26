package ru.kolaer.server.core.exception;

import ru.kolaer.common.dto.error.ErrorCode;

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
