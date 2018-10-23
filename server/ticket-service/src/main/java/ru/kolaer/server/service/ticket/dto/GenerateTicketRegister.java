package ru.kolaer.server.service.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.service.ticket.TypeOperation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTicketRegister {
    TypeOperation typeOperationForAll;
    Integer countForAll;
}
