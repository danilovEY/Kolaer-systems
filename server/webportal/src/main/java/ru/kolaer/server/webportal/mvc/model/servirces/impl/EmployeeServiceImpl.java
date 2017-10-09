package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.converter.EmployeeConverter;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeesDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdateEmployeesService;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends AbstractDefaultService<EmployeeDto, EmployeeEntity>
        implements EmployeeService, UpdateEmployeesService {
    private final EmployeeDao employeeDao;

    protected EmployeeServiceImpl(EmployeeDao employeeDao,
                                  EmployeeConverter converter) {
        super(employeeDao, converter);
        this.employeeDao = employeeDao;
    }

    @Override
    public EmployeeDto getByPersonnelNumber(Long id) {
        if(id == null || id < 1) {
            return null;
        }

        return baseConverter.convertToDto(employeeDao.findByPersonnelNumber(id));
    }

    @Override
    public List<EmployeeDto> getUserRangeBirthday(Date startData, Date endData) {
        return employeeDao.getUserRangeBirthday(startData, endData)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getUsersByBirthday(Date date) {
        return employeeDao.getUsersByBirthday(date)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getUserBirthdayToday() {
        return employeeDao.getUserBirthdayToday()
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getUsersByInitials(String initials) {
        return employeeDao.getUsersByInitials(initials)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getUsersByDepartmentId(Long id) {
        return employeeDao.findByDepartmentById(id)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeDto> getUsersByDepartmentId(int page, int pageSize, Long id) {
        if(page == 0) {
            List<EmployeeDto> employees = this.getUsersByDepartmentId(id);
            return new Page<>(employees, page, 0, employees.size());
        }

        Long count = employeeDao.findCountByDepartmentById(id);

        List<EmployeeDto> result = employeeDao.findByDepartmentById(page, pageSize, id)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());

        return new Page<>(result, page, count, pageSize);
    }

    @Override
    public int getCountUserBirthday(Date date) {
        return employeeDao.getCountUserBirthday(date);
    }

    @Override
    public ResultUpdateEmployeesDto updateEmployees(File file) {
        return this.employeeDao.updateEmployeesFromXlsx(file);
    }

    @Override
    public ResultUpdateEmployeesDto updateEmployees(InputStream inputStream) {
        return this.employeeDao.updateEmployeesFromXlsx(inputStream);
    }
}
