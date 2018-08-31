package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportDistributePipeDto {
    private String name;
    private long totalValue;
    private List<VacationReportDistributePipeValueDto> values;
}
