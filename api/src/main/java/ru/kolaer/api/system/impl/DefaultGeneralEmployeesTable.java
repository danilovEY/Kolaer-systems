package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultGeneralEmployeesTable implements GeneralEmployeesTable {
    private final List<EmployeeDto> entities;


    public DefaultGeneralEmployeesTable() {
        PostDto postEntity = new PostDto();
        postEntity.setAbbreviatedName("Должность");
        postEntity.setName("Моя должность");
        postEntity.setType(TypePostEnum.CATEGORY);
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

        this.entities = Collections.singletonList(employeeDto);

    }


    @Override
    public ServerResponse<List<EmployeeDto>> getAllUser() {
        return ServerResponse.createServerResponse(entities);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersMax(int maxCount) {
        return ServerResponse.createServerResponse(entities);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByBirthday(Date date) {
        return ServerResponse.createServerResponse(entities);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return ServerResponse.createServerResponse(entities);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersBirthdayToday() {
        return ServerResponse.createServerResponse(entities);
    }

    @Override
    public ServerResponse<List<EmployeeDto>> getUsersByInitials(String initials) {
            return ServerResponse.createServerResponse(entities);
    }

    @Override
    public ServerResponse<Integer> getCountUsersBirthday(Date date) {
        return ServerResponse.createServerResponse(0);
    }
}
