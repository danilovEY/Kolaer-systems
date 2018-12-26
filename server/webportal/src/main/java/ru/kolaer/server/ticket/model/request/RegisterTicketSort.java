package ru.kolaer.server.ticket.model.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class RegisterTicketSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId = SortType.DESC;
    @EntityFieldName(name = "createRegister")
    private SortType sortCreateRegister;
    @EntityFieldName(name = "sendRegisterTime")
    private SortType sortSendRegisterTime;
}
