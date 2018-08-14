package ru.kolaer.server.webportal.mvc.model.dto.employee;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.SortParam;
import ru.kolaer.server.webportal.mvc.model.dto.SortType;

@Data
public class EmployeeSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;

    @EntityFieldName(name = "initials")
    private SortType sortInitials;

    @EntityFieldName(name = "dismissalDate")
    private SortType sortDismissalDate;

    @EntityFieldName(name = "post.name")
    private SortType sortPostName;

    @EntityFieldName(name = "department.name")
    private SortType sortDepartmentName;

    @EntityFieldName(name = "personnelNumber")
    private SortType sortPersonnelNumber;

    @EntityFieldName(name = "firstName")
    private SortType sortFirstName;

    @EntityFieldName(name = "secondName")
    private SortType sortSecondName;

    @EntityFieldName(name = "thirdName")
    private SortType sortThirdName;

    @EntityFieldName(name = "gender")
    private SortType sortGender;

    @EntityFieldName(name = "category")
    private SortType sortCategory;

    @EntityFieldName(name = "birthday")
    private SortType sortBirthday;
}
