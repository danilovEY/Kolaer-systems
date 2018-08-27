package ru.kolaer.server.webportal.mvc.model.dto.employee;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.EnumCategory;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Data
public class EmployeeRequestDto {
    private String firstName;
    private Long personnelNumber;
    private String secondName;
    private String thirdName;
    private EnumGender gender;
    private Long departmentId;
    private Long postId;
    private Long typeWorkId;
    private Date birthday;
    private String email;
    private EnumCategory category;
}
