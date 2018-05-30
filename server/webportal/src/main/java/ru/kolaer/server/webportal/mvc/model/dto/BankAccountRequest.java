package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long employeeId;
    private String check;

}
