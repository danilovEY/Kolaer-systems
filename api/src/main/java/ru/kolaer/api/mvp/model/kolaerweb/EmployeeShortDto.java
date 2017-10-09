package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Data
public class EmployeeShortDto implements BaseDto {
    private Long id;
    private Long personnelNumber;
    private String initials;
    private String workPhoneNumber;
    private String homePhoneNumber;
    private EnumGender gender;
    private DepartmentShortDto department;
    private PostDto post;
    private Date birthday;
    private Date employmentDate;
    private Date dismissalDate;
    private String email;
    private String photo;
}
