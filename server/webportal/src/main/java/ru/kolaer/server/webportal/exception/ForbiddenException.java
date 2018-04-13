package ru.kolaer.server.webportal.exception;

import org.springframework.security.core.AuthenticationException;

public class ForbiddenException extends AuthenticationException {
    public ForbiddenException(String msg, Throwable t) {
        super(msg, t);
    }

    public ForbiddenException(String msg) {
        super(msg);
    }
}
