package ru.kolaer.server.service.otheremployee.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import employee.EmployeeOtherOrganizationConverter;
import employee.EmployeeOtherOrganizationEntity;
import employee.repository.EmployeeOtherOrganizationRepository;
import employee.service.EmployeeOtherOrganizationService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 03.11.2016.
 */
@Service
@Slf4j
public class EmployeeOtherOrganizationServiceImpl
        extends AbstractDefaultService<EmployeeOtherOrganizationDto, EmployeeOtherOrganizationEntity, EmployeeOtherOrganizationRepository, EmployeeOtherOrganizationConverter>
        implements EmployeeOtherOrganizationService {

    protected EmployeeOtherOrganizationServiceImpl(EmployeeOtherOrganizationRepository employeeOtherOrganizationDao,
                                                   EmployeeOtherOrganizationConverter converter) {
        super(employeeOtherOrganizationDao, converter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUserRangeBirthday(Date startData, Date endData) {
        return defaultEntityDao.getUserRangeBirthday(startData, endData)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUsersByBirthday(Date date) {
        return defaultEntityDao.getUsersByBirthday(date)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUserBirthdayToday() {
        return defaultEntityDao.getUserBirthdayToday()
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUsersByInitials(String initials) {
        return defaultEntityDao.getUsersByInitials(initials)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthday(Date date) {
        return defaultEntityDao.getCountUserBirthday(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeOtherOrganizationDto> getUsersByBirthdayAndOrg(Date date, String organization) {
        return defaultEntityDao.getUsersByBirthdayAndOrg(date, organization)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthdayAndOrg(Date date, String organization) {
        return defaultEntityDao.getCountUserBirthdayAndOrg(date, organization);
    }
}
