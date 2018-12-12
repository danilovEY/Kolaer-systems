package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VacationReportDistributeLineDto {
    private String name;
    private List<VacationReportDistributeLineValueDto> series = new ArrayList<>();
}
