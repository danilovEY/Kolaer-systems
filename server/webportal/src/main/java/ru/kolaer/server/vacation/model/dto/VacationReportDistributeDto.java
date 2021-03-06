package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VacationReportDistributeDto {
    private List<VacationReportDistributeLineDto> lineValues = new ArrayList<>();
    private List<VacationReportPipeDto> pipeValues = new ArrayList<>();
    private long maxSize;
}
