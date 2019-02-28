package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.employee.model.request.FindTypeWorkRequest;
import ru.kolaer.server.employee.service.TypeWorkService;

@RestController
@Api(tags = "Вид работ")
@Slf4j
public class TypeWorkController {
    private final TypeWorkService typeWorkService;

    public TypeWorkController(TypeWorkService typeWorkService) {
        this.typeWorkService = typeWorkService;
    }

    @ApiOperation(value = "Получить тип работы")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.TYPE_WORKS_READ + "')")
    @GetMapping(RouterConstants.TYPE_WORKS)
    public PageDto<TypeWorkDto> getTypeWorks(@ModelAttribute FindTypeWorkRequest request) {
        return typeWorkService.getAll(request);
    }

    @ApiOperation(value = "Добавить тип работы")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.TYPE_WORKS_WRITE + "')")
    @PostMapping(RouterConstants.TYPE_WORKS)
    public TypeWorkDto addTypeWork(@RequestBody TypeWorkDto request) {
        return typeWorkService.add(request);
    }

    @ApiOperation(value = "Редактировать тип работы")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.TYPE_WORKS_WRITE + "')")
    @PutMapping(RouterConstants.TYPE_WORKS_ID)
    public TypeWorkDto updateTypeWork(@PathVariable(PathVariableConstants.TYPE_WORK_ID) Long typeWorkId,
            @RequestBody TypeWorkDto request) {
        return typeWorkService.updateTypeWork(typeWorkId, request);
    }

    @ApiOperation(value = "Удалить тип работы")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.TYPE_WORKS_WRITE + "')")
    @DeleteMapping(RouterConstants.TYPE_WORKS_ID)
    public void deleteTypeWork(@PathVariable(PathVariableConstants.TYPE_WORK_ID) Long typeWorkId) {
        typeWorkService.deleteTypeWork(typeWorkId);
    }

}
