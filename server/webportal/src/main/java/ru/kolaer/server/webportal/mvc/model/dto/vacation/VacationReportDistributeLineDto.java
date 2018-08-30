package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportDistributeLineDto {
    private String name;
    private List<VacationReportDistributeLineValueDto> series;
}
