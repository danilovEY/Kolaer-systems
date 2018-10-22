package ru.kolaer.server.webportal.microservice.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTicketRegister {
    TypeOperation typeOperationForAll;
    Integer countForAll;
}
