package ru.kolaer.api.mvp.model.error;

/**
 * Created by danilovey on 11.10.2017.
 */
public enum ErrorCode {
    SERVER_ERROR("Ошибка на сервере"),
    UNAUTHORIZED("Не авторизован"),
    FORBIDDEN("У вас нет доступа"),
    CONTROLLER_NOT_FOUND("Контроллер не найден"),
    OBJECT_NOT_FOUND("Не найден объект"),
    SQL_EXCEPTION("Ошибка при SQL-запросе"),
    PRE_SQL_EXCEPTION("Ошибка перед SQL-запросом"),
    PARSE_EXCEPTION("Ошибка при парсинге"),
    INCORRECT_REQUEST_VALUE("Неизвестные значения в запросе"),
    CONNECT("Не удалось установить соединение");

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    private String message;
}
