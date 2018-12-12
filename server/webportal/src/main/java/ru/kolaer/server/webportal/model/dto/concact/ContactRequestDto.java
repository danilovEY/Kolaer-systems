package ru.kolaer.server.webportal.model.dto.concact;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.contact.ContactType;

@Data
public class ContactRequestDto {
    private String workPhoneNumber;
    private String mobilePhoneNumber;
    private String pager;
    private String email;
    private Long placementId;
    private ContactType type;
}
