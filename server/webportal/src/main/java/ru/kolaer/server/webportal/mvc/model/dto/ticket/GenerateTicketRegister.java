package ru.kolaer.server.webportal.mvc.model.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTicketRegister {
    TypeOperation typeOperationForAll;
    Integer countForAll;
}
