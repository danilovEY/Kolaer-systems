package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VacationReportPipeDto {
    private String name;
    private long totalValue;
    private List<VacationReportPipeValueDto> series = new ArrayList<>();
}
