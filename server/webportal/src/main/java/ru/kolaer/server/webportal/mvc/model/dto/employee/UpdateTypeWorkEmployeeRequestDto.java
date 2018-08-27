package ru.kolaer.server.webportal.mvc.model.dto.employee;

import lombok.Data;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Data
public class UpdateTypeWorkEmployeeRequestDto {
    private Long typeWorkId;
}
