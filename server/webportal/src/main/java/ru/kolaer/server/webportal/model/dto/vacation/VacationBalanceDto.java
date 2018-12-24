package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

@Data
public class VacationBalanceDto implements BaseDto {
    private Long id;
    private Long employeeId;
    private int nextYearBalance;
    private int currentYearBalance;
    private int prevYearBalance;
}
