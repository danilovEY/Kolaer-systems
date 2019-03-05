package ru.kolaer.server.employee.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.EmployeeConverter;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.EmployeeRequestDto;
import ru.kolaer.server.employee.model.dto.UpdateTypeWorkEmployeeRequestDto;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.employee.model.request.FindEmployeePageRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class EmployeeServiceImpl
        extends AbstractDefaultService<EmployeeDto, EmployeeEntity, EmployeeDao, EmployeeConverter>
        implements EmployeeService {

    private final AuthenticationService authenticationService;

    protected EmployeeServiceImpl(EmployeeDao employeeDao,
                                  EmployeeConverter converter,
                                  AuthenticationService authenticationService) {
        super(employeeDao, converter);
        this.authenticationService = authenticationService;
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
    public List<EmployeeDto> getUserRangeBirthday(LocalDate startData, LocalDate endData) {
        return defaultConverter.convertToDto(defaultEntityDao.getUserRangeBirthday(startData, endData));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getUsersByBirthday(LocalDate date) {
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
    public PageDto<EmployeeDto> getUsersByDepartmentId(int page, int pageSize, Long id) {
        Long count = defaultEntityDao.findCountByDepartmentById(id);

        List<EmployeeDto> result = defaultConverter
                .convertToDto(defaultEntityDao.findByDepartmentById(page, pageSize, id));

        return new PageDto<>(result, page, count, pageSize);
    }

    @Override
    @Transactional
    public EmployeeDto update(Long employeeId, EmployeeRequestDto employeeRequestDto) {
        if (!StringUtils.hasLength(employeeRequestDto.getFirstName()) ||
                !StringUtils.hasLength(employeeRequestDto.getSecondName()) ||
                !StringUtils.hasLength(employeeRequestDto.getThirdName())) {
            throw new UnexpectedRequestParams("ФИО не должно быть пустым");
        }

        if (employeeRequestDto.getDepartmentId() == null || employeeRequestDto.getPostId() == null) {
            throw new UnexpectedRequestParams("Должность и подразделение не должны быть пустыми");
        }


        EmployeeEntity employeeEntity = defaultEntityDao.findById(employeeId);
        if (employeeEntity == null) {
            throw new NotFoundDataException("Сотрудник не найден");
        }

        employeeEntity.setFirstName(employeeRequestDto.getFirstName());
        employeeEntity.setSecondName(employeeRequestDto.getSecondName());
        employeeEntity.setThirdName(employeeRequestDto.getThirdName());

        employeeEntity.setInitials(employeeEntity.getSecondName() + " " +
                employeeEntity.getFirstName() + " " + employeeEntity.getThirdName());

        employeeEntity.setTypeWorkId(employeeRequestDto.getTypeWorkId());
        employeeEntity.setDepartmentId(employeeRequestDto.getDepartmentId());
        employeeEntity.setPostId(employeeRequestDto.getPostId());
        employeeEntity.setBirthday(employeeRequestDto.getBirthday());
        employeeEntity.setGender(employeeRequestDto.getGender());
        employeeEntity.setCategory(employeeRequestDto.getCategory());
        employeeEntity.setHarmfulness(employeeRequestDto.isHarmfulness());

        return defaultConverter.convertToDto(defaultEntityDao.save(employeeEntity));
    }

    @Override
    @Transactional
    public EmployeeDto add(EmployeeRequestDto employeeRequestDto) {
        if (!StringUtils.hasLength(employeeRequestDto.getFirstName()) ||
                !StringUtils.hasLength(employeeRequestDto.getSecondName()) ||
                !StringUtils.hasLength(employeeRequestDto.getThirdName())) {
            throw new UnexpectedRequestParams("ФИО не должно быть пустым");
        }

        if (employeeRequestDto.getDepartmentId() == null || employeeRequestDto.getPostId() == null) {
            throw new UnexpectedRequestParams("Должность и подразделение не должны быть пустыми");
        }


        EmployeeEntity employeeEntity = defaultEntityDao.findByPersonnelNumber(employeeRequestDto.getPersonnelNumber());
        if (employeeEntity != null) {
            throw new UnexpectedRequestParams("Сотрудник с табельным номером уже существует");
        }
        employeeEntity = new EmployeeEntity();
        employeeEntity.setPersonnelNumber(employeeRequestDto.getPersonnelNumber());
        employeeEntity.setFirstName(employeeRequestDto.getFirstName());
        employeeEntity.setSecondName(employeeRequestDto.getSecondName());
        employeeEntity.setThirdName(employeeRequestDto.getThirdName());

        employeeEntity.setInitials(employeeEntity.getSecondName() + " " +
                employeeEntity.getFirstName() + " " + employeeEntity.getThirdName());

        employeeEntity.setTypeWorkId(employeeRequestDto.getTypeWorkId());
        employeeEntity.setDepartmentId(employeeRequestDto.getDepartmentId());
        employeeEntity.setPostId(employeeRequestDto.getPostId());
        employeeEntity.setBirthday(employeeRequestDto.getBirthday());
        employeeEntity.setGender(employeeRequestDto.getGender());
        employeeEntity.setCategory(employeeRequestDto.getCategory());
        employeeEntity.setEmploymentDate(new Date());

        return defaultConverter.convertToDto(defaultEntityDao.save(employeeEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<EmployeeDto> getEmployees(FindEmployeePageRequest request) {
        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!authenticationService.containsAccess(EmployeeAccessConstant.EMPLOYEES_READ)) {
            EmployeeEntity currentEmployee = Optional.ofNullable(accountAuthorized.getEmployeeId())
                    .map(defaultEntityDao::findById)
                    .orElseThrow(() -> new ForbiddenException("У вас нет доступа"));

            if(request.getDepartmentIds().size() > 1 ||
                    !request.getDepartmentIds().contains(currentEmployee.getDepartmentId())) {
                throw new ForbiddenException("У вас нет доступа");
            }
        }

        long count = defaultEntityDao.findAllEmployeeCount(request);
        List<EmployeeDto> employees = defaultConverter.convertToDto(defaultEntityDao.findAllEmployee(request));

        return new PageDto<>(employees, request.getPageNum(), count, request.getPageSize());
    }

    @Override
    @Transactional
    public EmployeeDto updateWorkType(Long employeeId, UpdateTypeWorkEmployeeRequestDto request) {
        if (request.getTypeWorkId() != null && request.getTypeWorkId() <= 0) {
            request.setTypeWorkId(null);
        }

        EmployeeEntity updatableEmployee = defaultEntityDao.findById(employeeId);
        updatableEmployee.setTypeWorkId(request.getTypeWorkId());
        updatableEmployee.setHarmfulness(request.isHarmfulness());

        return defaultConverter.convertToDto(defaultEntityDao.update(updatableEmployee));
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthday(LocalDate date) {
        return defaultEntityDao.getCountUserBirthday(date);
    }

    @Override
    @Transactional
    public long delete(Long id) {
        EmployeeEntity employeeEntity = this.defaultEntityDao.findById(id);
        employeeEntity.setDismissalDate(new Date());

        EmployeeDto employeeDto = defaultConverter.convertToDto(defaultEntityDao.save(employeeEntity));

//        ResultUpdate resultUpdate = new ResultUpdate();
//        resultUpdate.setDeleteEmployee(Collections.singletonList(employeeDto));
//        updatableEmployeeServices.forEach(service -> service.updateEmployee(resultUpdate));

        return 1;
    }
}
