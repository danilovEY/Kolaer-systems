package ru.kolaer.server.ticket.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.ticket.model.TypeOperation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTicketRegister {
    TypeOperation typeOperationForAll;
    Integer countForAll;
}
