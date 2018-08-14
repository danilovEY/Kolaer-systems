package ru.kolaer.server.webportal.mvc.model.dto.concact;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.dto.placement.PlacementDto;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactType;

@Data
public class ContactDto {
    private Long employeeId;
    private String photo;
    private String initials;
    private PostDto post;
    private DepartmentDto department;
    private String workPhoneNumber;
    private String mobilePhoneNumber;
    private String pager;
    private String email;
    private PlacementDto placement;
    private ContactType type;
}
