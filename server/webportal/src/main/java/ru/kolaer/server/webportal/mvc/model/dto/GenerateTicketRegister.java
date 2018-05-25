package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;

@Data
public class GenerateTicketRegister {
    TypeOperation typeOperationForAll;
    Integer countForAll;
}
