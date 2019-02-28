package ru.kolaer.server.employee.model.request;

import ru.kolaer.server.core.model.dto.request.SortFieldName;

public enum DepartmentSort implements SortFieldName {
    ID("id"),
    NAME("name"),
    CODE("code");

    public static DepartmentSort DEFAULT_SORT = ID;

    private final String fieldName;

    DepartmentSort(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
