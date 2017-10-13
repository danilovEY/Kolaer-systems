package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String workPhoneNumber;
    private String homePhoneNumber;
    private EnumGender gender;
    private DepartmentDto department;
    private PostDto post;
    private Date birthday;
    private Date employmentDate;
    private Date dismissalDate;
    private String email;
    private String photo;
    private EnumCategory category;

    public EmployeeDto(Long id) {
        this.id = id;
    }
}
