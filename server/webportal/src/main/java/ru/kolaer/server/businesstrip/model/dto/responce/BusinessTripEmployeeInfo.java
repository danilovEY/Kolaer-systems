package ru.kolaer.server.businesstrip.model.dto.responce;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.common.dto.post.PostDto;

@Data
public class BusinessTripEmployeeInfo {
    private String initials;
    private DepartmentDto department;
    private PostDto post;
}
