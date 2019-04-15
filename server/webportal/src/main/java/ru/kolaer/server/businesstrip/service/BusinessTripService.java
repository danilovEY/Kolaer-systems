package ru.kolaer.server.businesstrip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.businesstrip.model.dto.request.AddEmployeeToBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.CreateBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.EditBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.FindBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDetailDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripEmployeeDto;
import ru.kolaer.server.businesstrip.model.entity.BusinessTripEmployeeEntity;
import ru.kolaer.server.businesstrip.model.entity.BusinessTripEntity;
import ru.kolaer.server.businesstrip.repository.BusinessTripEmployeeRepository;
import ru.kolaer.server.businesstrip.repository.BusinessTripRepository;
import ru.kolaer.server.businesstrip.repository.BusinessTripSpecifications;
import ru.kolaer.server.core.converter.CommonConverter;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.dao.EmployeeDao;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class BusinessTripService {
    private final BusinessTripRepository businessTripRepository;
    private final BusinessTripEmployeeRepository businessTripEmployeeRepository;
    private final BusinessTripMapper businessTripMapper;
    private final EmployeeDao employeeDao;
    private final AuthenticationService authenticationService;

    @Autowired
    public BusinessTripService(BusinessTripRepository businessTripRepository,
            BusinessTripEmployeeRepository businessTripEmployeeRepository,
            BusinessTripMapper businessTripMapper,
            EmployeeDao employeeDao,
            AuthenticationService authenticationService) {
        this.businessTripRepository = businessTripRepository;
        this.businessTripEmployeeRepository = businessTripEmployeeRepository;
        this.businessTripMapper = businessTripMapper;
        this.employeeDao = employeeDao;
        this.authenticationService = authenticationService;
    }

    @Transactional(readOnly = true)
    public PageDto<BusinessTripDto> findAllBusinessTrip(@NotNull FindBusinessTripRequest request) {
        Page<BusinessTripEntity> businessTrips = businessTripRepository.findAll(BusinessTripSpecifications.findAll(request),
                request.toPageRequest(Sort.Direction.DESC, "id"));

        return CommonConverter.toPageDto(businessTrips, businessTripMapper::mapToBusinessTripDtos);
    }

    @Transactional(readOnly = true)
    public BusinessTripDetailDto getBusinessTripById(@Min(1) long businessTripId) {
        return businessTripRepository.findById(businessTripId)
                .map(businessTripMapper::mapToBusinessTripDetailDto)
                .orElseThrow(NotFoundDataException::new);
    }

    @Transactional
    public long createBusinessTrip(@NotNull CreateBusinessTripRequest request) {
        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        BusinessTripEntity businessTripEntity = businessTripMapper.mapToBusinessTripEntity(request);
        businessTripEntity.setWriterEmployeeId(accountAuthorized.getEmployeeId());

        return businessTripRepository.save(businessTripEntity).getId();
    }

    @Transactional
    public BusinessTripDetailDto editBusinessTrip(@Min(1) long businessTripId, @NotNull EditBusinessTripRequest request) {
        return null;
    }

    @Transactional
    public Long removeBusinessTripById(@Min(1) long businessTripId) {
        BusinessTripEntity businessTripEntity = businessTripRepository.findById(businessTripId)
                .orElseThrow(NotFoundDataException::new);

        businessTripEmployeeRepository.deleteAllByBusinessTripId(businessTripId);
        businessTripRepository.delete(businessTripEntity);

        return businessTripId;
    }

    @Transactional(readOnly = true)
    public List<BusinessTripEmployeeDto> getEmployeesByBusinessTripId(@Min(1) long businessTripId) {
        List<BusinessTripEmployeeEntity> employees = businessTripRepository.findById(businessTripId)
                .orElseThrow(NotFoundDataException::new)
                .getEmployees();

        return businessTripMapper.mapToBusinessTripEmployeeDtos(employees);
    }

    @Transactional
    public BusinessTripEmployeeDto addEmployeeToBusinessTrip(@Min(1) long businessTripId,
            @NotNull AddEmployeeToBusinessTripRequest request
    ) {
        return null;
    }

    @Transactional
    public long removeEmployeeToBusinessTrip(@Min(1) long businessTripId, @Min(1) long employeeId) {
        BusinessTripEmployeeEntity entity = businessTripEmployeeRepository
                .findByBusinessTripIdAndEmployeeId(businessTripId, employeeId)
                .orElseThrow(NotFoundDataException::new);

        businessTripEmployeeRepository.delete(entity);

        return employeeId;
    }
}
