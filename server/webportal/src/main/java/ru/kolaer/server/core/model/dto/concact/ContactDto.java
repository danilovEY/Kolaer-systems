package ru.kolaer.server.core.model.dto.concact;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.core.model.dto.placement.PlacementDto;

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
