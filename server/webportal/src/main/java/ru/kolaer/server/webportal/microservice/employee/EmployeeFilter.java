package ru.kolaer.server.webportal.microservice.employee;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.EnumCategory;
import ru.kolaer.common.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.FilterParam;
import ru.kolaer.server.webportal.common.dto.FilterType;

import java.time.LocalDate;

@Data
public class EmployeeFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;

    @EntityFieldName(name = "initials")
    private String filterInitials;

    @EntityFieldName(name = "dismissalDate")
    private Boolean filterDismissalDate;
    private FilterType typeFilterDismissalDate;

    @EntityFieldName(name = "post.name")
    private String filterPostName;

    @EntityFieldName(name = "department.name")
    private String filterDepartmentName;

    @EntityFieldName(name = "personnelNumber")
    private Long filterPersonnelNumber;
    private FilterType typeFilterPersonnelNumber = FilterType.EQUAL;

    @EntityFieldName(name = "firstName")
    private String filterFirstName;

    @EntityFieldName(name = "secondName")
    private String filterSecondName;

    @EntityFieldName(name = "thirdName")
    private String filterThirdName;

    @EntityFieldName(name = "gender")
    private EnumGender filterGender;
    private FilterType typeFilterGender = FilterType.EQUAL;

    @EntityFieldName(name = "category")
    private EnumCategory filterCategory;
    private FilterType typeFilterCategory = FilterType.EQUAL;

    @EntityFieldName(name = "birthday")
    private LocalDate filterBirthday;

}
