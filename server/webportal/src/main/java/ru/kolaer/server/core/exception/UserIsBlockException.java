package ru.kolaer.server.core.exception;

public class UserIsBlockException extends ForbiddenException {
    public UserIsBlockException() {
        this("Вы заблокированны");
    }

    public UserIsBlockException(String message) {
        super(message);
    }
}
