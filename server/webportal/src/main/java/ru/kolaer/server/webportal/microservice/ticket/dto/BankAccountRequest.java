package ru.kolaer.server.webportal.microservice.ticket.dto;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long employeeId;
    private String check;

}
