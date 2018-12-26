package ru.kolaer.server.ticket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.ticket.model.TypeOperation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendTicketDto {
    private String initials;
    private String check;
    private TypeOperation typeOperation;
    private Integer count;
}
