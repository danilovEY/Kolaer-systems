package ru.kolaer.server.businesstrip.model.dto.responce;

import lombok.Data;

@Data
public class BusinessTripEmployeeInfo {
    private long id;
    private String initials;
    private String departmentName;
    private String postName;
}
