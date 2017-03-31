package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Data
public class EmployeeEntityBase implements EmployeeEntity {
    private Integer id;
    private Integer personnelNumber;
    private String initials;
    private String workPhoneNumber;
    private String homePhoneNumber;
    private String gender;
    private DepartmentEntity department;
    private PostEntity postEntity;
    private Date birthday;
    private Date employmentDate;
    private Date dismissalDate;
    private String email;
    private String photo;
}
