package ru.kolaer.server.core.model.dto.concact;

import lombok.Data;

@Data
public class ContactRequestDto {
    private String workPhoneNumber;
    private String mobilePhoneNumber;
    private String pager;
    private Long placementId;
}
