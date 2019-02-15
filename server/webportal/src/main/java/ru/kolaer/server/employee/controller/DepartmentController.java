package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.employee.EmployeeAccessConstant;
import ru.kolaer.server.employee.model.dto.DepartmentRequestDto;
import ru.kolaer.server.employee.model.request.DepartmentFilter;
import ru.kolaer.server.employee.model.request.DepartmentSort;
import ru.kolaer.server.employee.model.request.FindDepartmentPageRequest;
import ru.kolaer.server.employee.service.DepartmentService;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Подразделения")
@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation(value = "Получить все подразделения")
    @GetMapping(RouterConstants.DEPARTMENTS)
    @PreAuthorize("permitAll()")
    public Page<DepartmentDto> getAllDepartment(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                DepartmentSort sortParam,
                                                DepartmentFilter filter) {
        return departmentService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Найти подразделения")
    @GetMapping(RouterConstants.DEPARTMENTS_FIND)
    @PreAuthorize("permitAll()")
    public Page<DepartmentDto> getAllDepartment(@ModelAttribute FindDepartmentPageRequest request) {
        return departmentService.find(request);
    }

    @ApiOperation(value = "Добавить подразделение")
    @PostMapping(RouterConstants.DEPARTMENTS)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.DEPARTMENTS_ADD + "')")
    public DepartmentDto addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.add(departmentRequestDto);
    }

    @ApiOperation(value = "Обновит подразделение")
    @PutMapping(RouterConstants.DEPARTMENTS_ID)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.DEPARTMENTS_EDIT + "')")
    public DepartmentDto updateDepartment(@PathVariable(PathVariableConstants.DEPARTMENT_ID) Long depId,
            @RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.update(depId, departmentRequestDto);
    }

    @ApiOperation(value = "Удалить подразделение")
    @DeleteMapping(RouterConstants.DEPARTMENTS_ID)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.DEPARTMENTS_DELETE + "')")
    public void deleteDepartment(@PathVariable(PathVariableConstants.DEPARTMENT_ID) Long depId) {
        departmentService.delete(depId, true);
    }

}
