package ru.kolaer.server.ticket.model.request;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long employeeId;
    private String check;

}
