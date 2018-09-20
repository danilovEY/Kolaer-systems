package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VacationReportPipeDto {
    private String name;
    private long totalValue;
    private List<VacationReportPipeValueDto> series = new ArrayList<>();
}
