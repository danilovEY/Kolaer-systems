package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.converter.EmployeeConverter;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends AbstractDefaultService<EmployeeDto, EmployeeEntity, EmployeeDao, EmployeeConverter> implements EmployeeService {

    protected EmployeeServiceImpl(EmployeeDao employeeDao,
                                  EmployeeConverter converter) {
        super(employeeDao, converter);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getByPersonnelNumber(Long id) {
        if(id == null || id < 1) {
            return null;
        }

        return defaultConverter.convertToDto(defaultEntityDao.findByPersonnelNumber(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getUserRangeBirthday(Date startData, Date endData) {
        return defaultConverter.convertToDto(defaultEntityDao.getUserRangeBirthday(startData, endData));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getUsersByBirthday(Date date) {
        return defaultConverter.convertToDto(defaultEntityDao.getUsersByBirthday(date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getUserBirthdayToday() {
        return defaultConverter.convertToDto(defaultEntityDao.getUserBirthdayToday());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getUsersByInitials(String initials) {
        return defaultConverter.convertToDto(defaultEntityDao.getUsersByInitials(initials));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getUsersByDepartmentId(Long id) {
        return defaultConverter.convertToDto(defaultEntityDao.findByDepartmentById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getUsersByDepartmentId(int page, int pageSize, Long id) {
        if(page == 0) {
            List<EmployeeDto> employees = this.getUsersByDepartmentId(id);
            return new Page<>(employees, page, 0, employees.size());
        }

        Long count = defaultEntityDao.findCountByDepartmentById(id);

        List<EmployeeDto> result = defaultConverter
                .convertToDto(defaultEntityDao.findByDepartmentById(page, pageSize, id));

        return new Page<>(result, page, count, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthday(Date date) {
        return defaultEntityDao.getCountUserBirthday(date);
    }
}
