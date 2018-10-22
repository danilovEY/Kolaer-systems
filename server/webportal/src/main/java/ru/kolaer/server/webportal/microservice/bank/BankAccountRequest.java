package ru.kolaer.server.webportal.microservice.bank;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long employeeId;
    private String check;

}
