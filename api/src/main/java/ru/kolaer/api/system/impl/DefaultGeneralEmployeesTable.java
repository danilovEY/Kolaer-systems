package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;

import java.util.Date;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultGeneralEmployeesTable implements GeneralEmployeesTable {
    private final EmployeeEntity[] entities = new EmployeeEntity[1];


    public DefaultGeneralEmployeesTable() {
        PostEntity postEntity = new PostEntityBase();
        postEntity.setId(1);
        postEntity.setAbbreviatedName("Должность");
        postEntity.setName("Моя должность");
        postEntity.setTypeRang(TypeRangEnum.CATEGORY.getName());
        postEntity.setRang(1);

        DepartmentEntity departmentEntity = new DepartmentEntityBase();
        departmentEntity.setId(1);
        departmentEntity.setAbbreviatedName("Подразделение");
        departmentEntity.setName("Мое подразделение");
        departmentEntity.setChiefEntity(1);

        EmployeeEntityBase employeeEntityBase = new EmployeeEntityBase();
        employeeEntityBase.setPersonnelNumber(1);
        employeeEntityBase.setId(1);
        employeeEntityBase.setMobileNumber("79876543210");
        employeeEntityBase.setPhoneNumber("76543");
        employeeEntityBase.setGender("Неизвестный");
        employeeEntityBase.setBirthday(new Date());
        employeeEntityBase.setDepartment(departmentEntity);
        employeeEntityBase.setPostEntity(postEntity);

        this.entities[0] = employeeEntityBase;

    }

    @Override
    public EmployeeEntity[] getAllUser() {
        return this.entities;
    }

    @Override
    public EmployeeEntity[] getUsersMax(int maxCount) {
        return this.entities;
    }

    @Override
    public EmployeeEntity[] getUsersByBirthday(Date date) {
        return this.entities;
    }

    @Override
    public EmployeeEntity[] getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return this.entities;
    }

    @Override
    public EmployeeEntity[] getUsersBirthdayToday() {
        return this.entities;
    }

    @Override
    public EmployeeEntity[] getUsersByInitials(String initials) {
        return this.entities;
    }

    @Override
    public int getCountUsersBirthday(Date date) {
        return 1;
    }
}
