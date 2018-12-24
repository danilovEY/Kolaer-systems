package ru.kolaer.common.dto.kolaerweb;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.common.dto.post.PostDto;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Data
@NoArgsConstructor
public class EmployeeDto implements BaseDto {
    private Long id;
    private Long personnelNumber;
    private String initials;
    private String firstName;
    private String secondName;
    private String thirdName;
    private EnumGender gender;
    private DepartmentDto department;
    private PostDto post;
    private Date birthday;
    private Date employmentDate;
    private Date dismissalDate;
    private String photo;
    private EnumCategory category;
    private boolean harmfulness;
    private TypeWorkDto typeWork;

    public EmployeeDto(Long id) {
        this.id = id;
    }
}
