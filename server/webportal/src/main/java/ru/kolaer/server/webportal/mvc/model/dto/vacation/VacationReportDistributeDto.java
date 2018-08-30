package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class VacationReportDistributeDto {
    private List<VacationReportDistributeLineDto> lineValues = Collections.emptyList();
    private List<VacationReportDistributePipeDto> pipeValues = Collections.emptyList();
}
