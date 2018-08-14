package ru.kolaer.server.webportal.mvc.model.dto.ticket;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.SortParam;
import ru.kolaer.server.webportal.mvc.model.dto.SortType;

@Data
public class RegisterTicketSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId = SortType.DESC;
    @EntityFieldName(name = "createRegister")
    private SortType sortCreateRegister;
    @EntityFieldName(name = "sendRegisterTime")
    private SortType sortSendRegisterTime;
}
