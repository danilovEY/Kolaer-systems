package ru.kolaer.server.businesstrip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.businesstrip.BusinessTripAccessConstant;
import ru.kolaer.server.businesstrip.model.dto.request.AddEmployeeToBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.CreateBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.EditBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.request.FindBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDetailDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDto;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripEmployeeDto;
import ru.kolaer.server.businesstrip.service.BusinessTripService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class BusinessTripController {

    private final BusinessTripService businessTripService;

    @Autowired
    public BusinessTripController(BusinessTripService businessTripService) {
        this.businessTripService = businessTripService;
    }

    @GetMapping(RouterConstants.BUSINESS_TRIP)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_READ + "')")
    public PageDto<BusinessTripDto> findAllBusinessTrip(@ModelAttribute FindBusinessTripRequest request) {
        return businessTripService.findAllBusinessTrip(request);
    }

    @PostMapping(RouterConstants.BUSINESS_TRIP)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_WRITE + "')")
    public Long createBusinessTrip(@RequestBody CreateBusinessTripRequest request) {
        return businessTripService.createBusinessTrip(request);
    }

    @PutMapping(RouterConstants.BUSINESS_TRIP_ID)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_WRITE + "')")
    public BusinessTripDetailDto editBusinessTrip(
            @PathVariable(PathVariableConstants.BUSINESS_TRIP_ID) @Min(1) long businessTripId,
            @RequestBody EditBusinessTripRequest request
    ) {
        return businessTripService.editBusinessTrip(businessTripId, request);
    }

    @GetMapping(RouterConstants.BUSINESS_TRIP_ID)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_READ + "')")
    public BusinessTripDetailDto getBusinessTrip(
            @PathVariable(PathVariableConstants.BUSINESS_TRIP_ID) @Min(1) long businessTripId
    ) {
        return businessTripService.getBusinessTripById(businessTripId);
    }

    @DeleteMapping(RouterConstants.BUSINESS_TRIP_ID)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_WRITE + "')")
    public Long removeBusinessTrip(@PathVariable(PathVariableConstants.BUSINESS_TRIP_ID) @Min(1) long businessTripId) {
        return businessTripService.removeBusinessTripById(businessTripId);
    }

    @GetMapping(RouterConstants.BUSINESS_TRIP_ID_EMPLOYEES)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_READ + "')")
    public List<BusinessTripEmployeeDto> getEmployeesByBusinessTrip(
            @PathVariable(PathVariableConstants.BUSINESS_TRIP_ID) @Min(1) long businessTripId) {
        return businessTripService.getEmployeesByBusinessTripId(businessTripId);
    }

    @PostMapping(RouterConstants.BUSINESS_TRIP_ID_EMPLOYEES)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_WRITE + "')")
    public BusinessTripEmployeeDto addEmployeeToBusinessTrip(
            @PathVariable(PathVariableConstants.BUSINESS_TRIP_ID) @Min(1) long businessTripId,
            @RequestBody AddEmployeeToBusinessTripRequest request
    ) {
        return businessTripService.addEmployeeToBusinessTrip(businessTripId, request);
    }

    @DeleteMapping(RouterConstants.BUSINESS_TRIP_ID_EMPLOYEE_ID)
    @PreAuthorize("hasRole('" + BusinessTripAccessConstant.BUSINESS_TRIP_WRITE + "')")
    public Long removeEmployeeToBusinessTrip(
            @PathVariable(PathVariableConstants.BUSINESS_TRIP_ID) @Min(1) long businessTripId,
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return businessTripService.removeEmployeeToBusinessTrip(businessTripId, employeeId);
    }

}
