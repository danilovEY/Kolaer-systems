package ru.kolaer.server.businesstrip.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDestinationDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripEmployeeInfo;
import ru.kolaer.server.businesstrip.model.entity.BusinessTripEmployeeEntity;
import ru.kolaer.server.businesstrip.model.entity.BusinessTripEntity;
import ru.kolaer.server.businesstrip.repository.BusinessTripEmployeeRepository;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.dao.PostDao;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.employee.model.entity.PostEntity;
import ru.kolaer.server.employee.repository.DepartmentRepository;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Validated
public class BusinessTripMapper {
    private final BusinessTripEmployeeRepository businessTripEmployeeRepository;
    private final EmployeeDao employeeDao;
    private final DepartmentRepository departmentRepository;
    private final PostDao postDao;

    public BusinessTripMapper(BusinessTripEmployeeRepository businessTripEmployeeRepository, EmployeeDao employeeDao,
            DepartmentRepository departmentRepository, PostDao postDao
    ) {
        this.businessTripEmployeeRepository = businessTripEmployeeRepository;
        this.employeeDao = employeeDao;
        this.departmentRepository = departmentRepository;
        this.postDao = postDao;
    }

    public List<BusinessTripDto> mapToBusinessTripDtos(@NotNull List<BusinessTripEntity> entities) {
        if (entities.isEmpty()) return Collections.emptyList();

        Set<Long> businessTripIds = entities.stream()
                .map(BusinessTripEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, List<BusinessTripEmployeeEntity>> businessTripEmployeeMap = businessTripEmployeeRepository
                .findAllByBusinessTripIdIsIn(businessTripIds)
                .stream()
                .collect(Collectors.groupingBy(BusinessTripEmployeeEntity::getBusinessTripId));

        Set<Long> employeeIds = businessTripEmployeeMap
                .values()
                .stream()
                .flatMap(Collection::stream)
                .map(BusinessTripEmployeeEntity::getEmployeeId)
                .collect(Collectors.toSet());

        Map<Long, EmployeeEntity> employeeMap = employeeDao.findById(employeeIds)
                .stream()
                .collect(Collectors.toMap(EmployeeEntity::getId, Function.identity()));

        Set<Long> departmentIds = employeeMap.values()
                .stream()
                .map(EmployeeEntity::getDepartmentId)
                .collect(Collectors.toSet());

        Set<Long> postIds = employeeMap.values()
                .stream()
                .map(EmployeeEntity::getPostId)
                .collect(Collectors.toSet());

        Map<Long, DepartmentEntity> departmentMap = departmentRepository.findAllById(departmentIds)
                .stream()
                .collect(Collectors.toMap(DepartmentEntity::getId, Function.identity()));

        Map<Long, PostEntity> postMap = postDao.findById(postIds)
                .stream()
                .collect(Collectors.toMap(PostEntity::getId, Function.identity()));

        return entities.stream()
                .map(businessTrip ->
                        mapToBusinessTripDto(
                                businessTrip,
                                businessTripEmployeeMap.get(businessTrip.getId()),
                                employeeMap,
                                departmentMap,
                                postMap
                        )
                )
                .collect(Collectors.toList());
    }

    public BusinessTripDto mapToBusinessTripDto(@NotNull BusinessTripEntity entity) {
        List<BusinessTripEmployeeEntity> employees = entity.getEmployees();

        Set<Long> employeeIds = employees
                .stream()
                .map(BusinessTripEmployeeEntity::getEmployeeId)
                .collect(Collectors.toSet());
        employeeIds.add(entity.getWriterEmployeeId());

        Map<Long, EmployeeEntity> employeeMap = employeeDao.findById(employeeIds)
                .stream()
                .collect(Collectors.toMap(EmployeeEntity::getId, Function.identity()));

        Set<Long> departmentIds = employeeMap.values()
                .stream()
                .map(EmployeeEntity::getDepartmentId)
                .collect(Collectors.toSet());

        Set<Long> postIds = employeeMap.values()
                .stream()
                .map(EmployeeEntity::getPostId)
                .collect(Collectors.toSet());

        Map<Long, DepartmentEntity> departmentMap = departmentRepository.findAllById(departmentIds)
                .stream()
                .collect(Collectors.toMap(DepartmentEntity::getId, Function.identity()));

        Map<Long, PostEntity> postMap = postDao.findById(postIds)
                .stream()
                .collect(Collectors.toMap(PostEntity::getId, Function.identity()));

        return mapToBusinessTripDto(entity, employees, employeeMap, departmentMap, postMap);
    }

    private BusinessTripDto mapToBusinessTripDto(@NotNull BusinessTripEntity entity, List<BusinessTripEmployeeEntity> employees,
            Map<Long, EmployeeEntity> employeeMap, Map<Long, DepartmentEntity> departmentMap, Map<Long, PostEntity> postMap
    ) {
        BusinessTripDto businessTrip = new BusinessTripDto();
        businessTrip.setId(entity.getId());
        businessTrip.setBusinessTripType(entity.getBusinessTripType());
        businessTrip.setComment(entity.getComment());
        businessTrip.setDocumentDate(entity.getDocumentDate());
        businessTrip.setDocumentNumber(entity.getDocumentNumber());
        businessTrip.setOrganizationName(entity.getOrganizationName());

        EmployeeEntity writerEmployee = employeeMap.get(entity.getWriterEmployeeId());

        List<BusinessTripDestinationDto> destinations = employees.stream()
                .map(this::mapToBusinessTripDestinationDto)
                .collect(Collectors.toList());

        List<BusinessTripEmployeeInfo> businessTripEmployees = employees.stream()
                .map(businessTripEmployee -> {
                    EmployeeEntity employee = employeeMap.get(businessTripEmployee.getEmployeeId());

                    return mapToBusinessTripEmployeeInfo(
                            employee,
                            departmentMap.get(employee.getDepartmentId()),
                            postMap.get(employee.getPostId())
                    );
                })
                .collect(Collectors.toList());

        businessTrip.setDestinations(destinations);
        businessTrip.setEmployees(businessTripEmployees);
        businessTrip.setWriterEmployee(mapToBusinessTripEmployeeInfo(
                writerEmployee,
                departmentMap.get(writerEmployee.getDepartmentId()),
                postMap.get(writerEmployee.getPostId())
        ));

        return businessTrip;
    }

    private BusinessTripEmployeeInfo mapToBusinessTripEmployeeInfo(@NotNull EmployeeEntity employee,
            @NotNull DepartmentEntity department, @NotNull PostEntity post
    ) {
        BusinessTripEmployeeInfo businessTripEmployeeInfo = new BusinessTripEmployeeInfo();
        businessTripEmployeeInfo.setInitials(employee.getInitials());
        businessTripEmployeeInfo.setDepartmentName(department.getAbbreviatedName());
        businessTripEmployeeInfo.setPostName(post.getAbbreviatedName());
        return businessTripEmployeeInfo;
    }

    private BusinessTripDestinationDto mapToBusinessTripDestinationDto(@NotNull BusinessTripEmployeeEntity entity) {
        BusinessTripDestinationDto businessTripDestinationDto = new BusinessTripDestinationDto();
        businessTripDestinationDto.setDestinationCity(entity.getDestinationCity());
        businessTripDestinationDto.setDestinationCountry(entity.getDestinationCountry());
        businessTripDestinationDto.setDestinationOrganizationName(entity.getDestinationOrganizationName());

        return businessTripDestinationDto;
    }
}
