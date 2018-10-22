package ru.kolaer.server.webportal.microservice.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendTicketDto {
    private String initials;
    private String check;
    private TypeOperation typeOperation;
    private Integer count;
}
