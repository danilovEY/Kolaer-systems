package ru.kolaer.server.businesstrip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.businesstrip.model.dto.request.AddEmployeeToBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.CreateBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.EditBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.FindBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDetailDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripEmployeeDto;
import ru.kolaer.server.businesstrip.repository.BusinessTripEmployeeRepository;
import ru.kolaer.server.businesstrip.repository.BusinessTripRepository;
import ru.kolaer.server.employee.dao.EmployeeDao;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Service
@Validated
public class BusinessTripService {
    private final BusinessTripRepository businessTripRepository;
    private final BusinessTripEmployeeRepository businessTripEmployeeRepository;
    private final BusinessTripMapper businessTripMapper;
    private final EmployeeDao employeeDao;

    @Autowired
    public BusinessTripService(BusinessTripRepository businessTripRepository,
            BusinessTripEmployeeRepository businessTripEmployeeRepository, BusinessTripMapper businessTripMapper,
            EmployeeDao employeeDao
    ) {
        this.businessTripRepository = businessTripRepository;
        this.businessTripEmployeeRepository = businessTripEmployeeRepository;
        this.businessTripMapper = businessTripMapper;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public PageDto<BusinessTripDto> findAllBusinessTrip(@NotNull FindBusinessTripRequest request) {
        return PageDto.createPage();
    }

    @Transactional
    public long createBusinessTrip(@NotNull CreateBusinessTripRequest request) {
        return 0L;
    }

    @Transactional
    public BusinessTripEmployeeDto addEmployeeToBusinessTrip(@Min(1) long businessTripId,
            @NotNull AddEmployeeToBusinessTripRequest request
    ) {
        return null;
    }

    @Transactional
    public long removeEmployeeToBusinessTrip(@Min(1) long businessTripId, @Min(1) long employeeId) {
        return 0L;
    }

    @Transactional
    public BusinessTripDetailDto editBusinessTrip(@Min(1) long businessTripId, @NotNull EditBusinessTripRequest request) {
        return null;
    }

    @Transactional
    public Long removeBusinessTripById(@Min(1) long businessTripId) {
        return null;
    }
}
