package ru.kolaer.server.webportal.microservice.ticket.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;
import ru.kolaer.server.webportal.common.dto.SortType;

@Data
public class RegisterTicketSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId = SortType.DESC;
    @EntityFieldName(name = "createRegister")
    private SortType sortCreateRegister;
    @EntityFieldName(name = "sendRegisterTime")
    private SortType sortSendRegisterTime;
}
