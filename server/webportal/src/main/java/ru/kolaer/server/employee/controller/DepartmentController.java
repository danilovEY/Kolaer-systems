package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.employee.model.dto.DepartmentRequestDto;
import ru.kolaer.server.employee.model.request.FindDepartmentPageRequest;
import ru.kolaer.server.employee.service.DepartmentService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Подразделения")
@RestController
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation(value = "Получить подразделения")
    @GetMapping(RouterConstants.DEPARTMENTS)
    public PageDto<DepartmentDto> getAllDepartment(@ModelAttribute @NotNull FindDepartmentPageRequest request) {
        return departmentService.find(request);
    }

    @ApiOperation(value = "Добавить подразделение")
    @PostMapping(RouterConstants.DEPARTMENTS)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.DEPARTMENTS_WRITE + "')")
    public DepartmentDto addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.add(departmentRequestDto);
    }

    @ApiOperation(value = "Обновит подразделение")
    @PutMapping(RouterConstants.DEPARTMENTS_ID)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.DEPARTMENTS_WRITE + "')")
    public DepartmentDto updateDepartment(@PathVariable(PathVariableConstants.DEPARTMENT_ID) @Min(1) long depId,
            @RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.update(depId, departmentRequestDto);
    }

    @ApiOperation(value = "Удалить подразделение")
    @DeleteMapping(RouterConstants.DEPARTMENTS_ID)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.DEPARTMENTS_WRITE + "')")
    public void deleteDepartment(@PathVariable(PathVariableConstants.DEPARTMENT_ID) @Min(1) long depId) {
        departmentService.delete(depId);
    }

}
