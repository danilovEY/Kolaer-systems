package ru.kolaer.server.webportal.model.dto.concact;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.webportal.model.dto.placement.PlacementDto;
import ru.kolaer.server.webportal.model.entity.contact.ContactType;

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
