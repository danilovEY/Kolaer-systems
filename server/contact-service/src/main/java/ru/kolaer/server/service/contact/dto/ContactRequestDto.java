package ru.kolaer.server.service.contact.dto;

import lombok.Data;
import ru.kolaer.server.service.contact.ContactType;

@Data
public class ContactRequestDto {
    private String workPhoneNumber;
    private String mobilePhoneNumber;
    private String pager;
    private String email;
    private Long placementId;
    private ContactType type;
}
