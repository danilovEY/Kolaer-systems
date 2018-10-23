package ru.kolaer.server.service.contact.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.service.contact.ContactType;
import placement.dto.PlacementDto;

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
