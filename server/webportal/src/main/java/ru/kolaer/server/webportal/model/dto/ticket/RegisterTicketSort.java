package ru.kolaer.server.webportal.model.dto.ticket;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.SortParam;
import ru.kolaer.server.webportal.model.dto.SortType;

@Data
public class RegisterTicketSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId = SortType.DESC;
    @EntityFieldName(name = "createRegister")
    private SortType sortCreateRegister;
    @EntityFieldName(name = "sendRegisterTime")
    private SortType sortSendRegisterTime;
}
