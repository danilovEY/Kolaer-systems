package ru.kolaer.server.webportal.model.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.webportal.model.entity.ticket.TypeOperation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendTicketDto {
    private String initials;
    private String check;
    private TypeOperation typeOperation;
    private Integer count;
}
