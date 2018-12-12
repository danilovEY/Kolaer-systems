package ru.kolaer.server.webportal.model.dto.bank;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long employeeId;
    private String check;

}
