package ru.kolaer.server.service.ticket.dto;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long employeeId;
    private String check;

}
