package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;

import java.util.Date;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultGeneralEmployeesTable implements GeneralEmployeesTable {
    private final EmployeeDto[] entities = new EmployeeDto[1];


    public DefaultGeneralEmployeesTable() {
        PostDto postEntity = new PostDto();
        postEntity.setAbbreviatedName("Должность");
        postEntity.setName("Моя должность");
        postEntity.setTypeRang(TypeRangEnum.CATEGORY.getName());
        postEntity.setRang(1);

        DepartmentDto departmentEntity = new DepartmentDto();
        departmentEntity.setAbbreviatedName("Подразделение");
        departmentEntity.setName("Мое подразделение");

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setWorkPhoneNumber("79876543210");
        employeeDto.setHomePhoneNumber("76543");
        employeeDto.setGender(EnumGender.MALE);
        employeeDto.setBirthday(new Date());
        employeeDto.setDepartment(departmentEntity);
        employeeDto.setPost(postEntity);

        this.entities[0] = employeeDto;

    }

    @Override
    public EmployeeDto[] getAllUser() {
        return this.entities;
    }

    @Override
    public EmployeeDto[] getUsersMax(int maxCount) {
        return this.entities;
    }

    @Override
    public EmployeeDto[] getUsersByBirthday(Date date) {
        return this.entities;
    }

    @Override
    public EmployeeDto[] getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return this.entities;
    }

    @Override
    public EmployeeDto[] getUsersBirthdayToday() {
        return this.entities;
    }

    @Override
    public EmployeeDto[] getUsersByInitials(String initials) {
        return this.entities;
    }

    @Override
    public int getCountUsersBirthday(Date date) {
        return 1;
    }
}
