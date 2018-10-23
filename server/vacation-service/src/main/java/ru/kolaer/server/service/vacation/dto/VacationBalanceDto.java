package ru.kolaer.server.service.vacation.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;

@Data
public class VacationBalanceDto implements BaseDto {
    private Long id;
    private Long employeeId;
    private int nextYearBalance;
    private int currentYearBalance;
    private int prevYearBalance;
}
