package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VacationReportDistributeDto {
    private List<VacationReportDistributeLineDto> lineValues = new ArrayList<>();
    private List<VacationReportPipeDto> pipeValues = new ArrayList<>();
    private long maxSize;
}
