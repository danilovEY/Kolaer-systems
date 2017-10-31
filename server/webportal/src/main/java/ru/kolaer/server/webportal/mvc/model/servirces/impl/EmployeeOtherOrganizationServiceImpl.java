package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.webportal.mvc.model.converter.EmployeeOtherOrganizationConverter;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeOtherOrganizationDao;
import ru.kolaer.server.webportal.mvc.model.entities.birthday.EmployeeOtherOrganizationEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeOtherOrganizationService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 03.11.2016.
 */
@Service
@Slf4j
public class EmployeeOtherOrganizationServiceImpl
        extends AbstractDefaultService<EmployeeOtherOrganizationDto, EmployeeOtherOrganizationEntity>
        implements EmployeeOtherOrganizationService {

    private final EmployeeOtherOrganizationDao employeeOtherOrganizationDao;

    protected EmployeeOtherOrganizationServiceImpl(EmployeeOtherOrganizationDao employeeOtherOrganizationDao,
                                                   EmployeeOtherOrganizationConverter converter) {
        super(employeeOtherOrganizationDao, converter);
        this.employeeOtherOrganizationDao = employeeOtherOrganizationDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUserRangeBirthday(Date startData, Date endData) {
        return employeeOtherOrganizationDao.getUserRangeBirthday(startData, endData)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUsersByBirthday(Date date) {
        return employeeOtherOrganizationDao.getUsersByBirthday(date)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUserBirthdayToday() {
        return employeeOtherOrganizationDao.getUserBirthdayToday()
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUsersByInitials(String initials) {
        return employeeOtherOrganizationDao.getUsersByInitials(initials)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthday(Date date) {
        return employeeOtherOrganizationDao.getCountUserBirthday(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUsersByBirthdayAndOrg(Date date, String organization) {
        return employeeOtherOrganizationDao.getUsersByBirthdayAndOrg(date, organization)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthdayAndOrg(Date date, String organization) {
        return employeeOtherOrganizationDao.getCountUserBirthdayAndOrg(date, organization);
    }
}
