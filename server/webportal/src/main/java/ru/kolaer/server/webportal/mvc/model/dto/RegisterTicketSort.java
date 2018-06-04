package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class RegisterTicketSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId = SortType.DESC;
    @EntityFieldName(name = "createRegister")
    private SortType sortCreateRegister;
    @EntityFieldName(name = "sendRegisterTime")
    private SortType sortSendRegisterTime;
}
