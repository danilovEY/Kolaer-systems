package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
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
@RequestMapping(value = "/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation(value = "Получить все подразделения")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<DepartmentDto> getAllDepartment(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                DepartmentSort sortParam,
                                                DepartmentFilter filter) {
        return departmentService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Найти подразделения")
    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<DepartmentDto> getAllDepartment(@ModelAttribute FindDepartmentPageRequest request) {
        return departmentService.find(request);
    }

    @ApiOperation(value = "Добавить подразделение")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DepartmentDto addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.add(departmentRequestDto);
    }

    @ApiOperation(value = "Обновит подразделение")
    @RequestMapping(value = "/{depId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DepartmentDto updateDepartment(@PathVariable("depId") Long depId,
                                  @RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.update(depId, departmentRequestDto);
    }

    @ApiOperation(value = "Удалить подразделение")
    @RequestMapping(value = "/{depId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteDepartment(@PathVariable("depId") Long depId) {
        departmentService.delete(depId, true);
    }

}
