package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VacationReportScheduledDto {
    private List<VacationReportScheduledPipeDto> pipeValues = new ArrayList<>();
    private long maxSize;
}
