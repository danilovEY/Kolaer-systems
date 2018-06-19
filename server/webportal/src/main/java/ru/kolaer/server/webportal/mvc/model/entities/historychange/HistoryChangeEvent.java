package ru.kolaer.server.webportal.mvc.model.entities.historychange;

public enum HistoryChangeEvent {
    UPLOAD_FILE_TO_UPDATE_EMPLOYEE("Загрузка файла для обновление сотрудников"),
    UNKNOWN("Неизвестно"),
    HIDE_POST("Удаление должности"),
    UPDATE_POST("Обновление должности"),
    HIDE_DEPARTMENT("Удаление подразделения"),
    UPDATE_DEPARTMENT("Обновление подразделения"),
    HIDE_EMPLOYEE("Удаление сотрудника"),
    UPDATE_EMPLOYEE("Обновление сотрудника"),
    ADD_DEPARTMENT("Добавление подразделения"),
    ADD_POST("Добавление дожлности"),
    ADD_EMPLOYEE("Добавление сотрудника");

    private String description;

    HistoryChangeEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
