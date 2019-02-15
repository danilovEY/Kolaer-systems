package ru.kolaer.common.dto.employee;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.DepartmentShortDto;
import ru.kolaer.common.dto.post.PostDto;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Data
public class EmployeeWithoutPersonalDataDto implements BaseDto {
    private Long id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String workPhoneNumber;
    private EnumGender gender;
    private DepartmentShortDto department;
    private PostDto post;
    private Date birthday;
    private String email;
    private String photo;
}
