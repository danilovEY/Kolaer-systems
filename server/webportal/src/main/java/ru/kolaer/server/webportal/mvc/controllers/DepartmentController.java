package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.department.DepartmentFilter;
import ru.kolaer.server.webportal.mvc.model.dto.department.DepartmentRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.department.DepartmentSort;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;

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
    @UrlDeclaration(description = "Получить все подразделения")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<DepartmentDto> getAllDepartment(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                DepartmentSort sortParam,
                                                DepartmentFilter filter) {
        return departmentService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Добавить подразделение")
    @UrlDeclaration(description = "Добавить подразделение", isUser = false, requestMethod = RequestMethod.POST, isOk = true)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DepartmentDto addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.add(departmentRequestDto);
    }

    @ApiOperation(value = "Обновит подразделение")
    @UrlDeclaration(description = "Обновит подразделение", isUser = false, requestMethod = RequestMethod.PUT, isOk = true)
    @RequestMapping(value = "/{depId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DepartmentDto updateDepartment(@PathVariable("depId") Long depId,
                                  @RequestBody DepartmentRequestDto departmentRequestDto) {
        return departmentService.update(depId, departmentRequestDto);
    }

    @ApiOperation(value = "Удалить подразделение")
    @UrlDeclaration(description = "Удалить подразделение", isUser = false, requestMethod = RequestMethod.DELETE, isOk = true)
    @RequestMapping(value = "/{depId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteDepartment(@PathVariable("depId") Long depId) {
        departmentService.delete(depId, true);
    }

}
