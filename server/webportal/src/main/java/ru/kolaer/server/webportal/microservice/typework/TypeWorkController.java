package ru.kolaer.server.webportal.microservice.typework;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;

@RestController
@RequestMapping(value = "/type-work")
@Api(tags = "Вид работ")
@Slf4j
public class TypeWorkController {
    private final TypeWorkService typeWorkService;

    public TypeWorkController(TypeWorkService typeWorkService) {
        this.typeWorkService = typeWorkService;
    }

    @ApiOperation(value = "Получить тип работы")
    @UrlDeclaration(isUser = false, isTypeWork = true, isOk = true)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<TypeWorkDto> getTypeWorks(@ModelAttribute FindTypeWorkRequest request) {
        return typeWorkService.getAll(request);
    }

    @ApiOperation(value = "Добавить тип работы")
    @UrlDeclaration(isUser = false, isTypeWork = true, isOk = true)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TypeWorkDto addTypeWork(@RequestBody TypeWorkDto request) {
        return typeWorkService.add(request);
    }

    @ApiOperation(value = "Редактировать тип работы")
    @UrlDeclaration(isUser = false, isTypeWork = true, isOk = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TypeWorkDto updateTypeWork(@PathVariable("id") Long typeWorkId,
                                       @RequestBody TypeWorkDto request) {
        return typeWorkService.updateTypeWork(typeWorkId, request);
    }

    @ApiOperation(value = "Удалить тип работы")
    @UrlDeclaration(isUser = false, isTypeWork = true, isOk = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTypeWork(@PathVariable("id") Long typeWorkId) {
        typeWorkService.deleteTypeWork(typeWorkId);
    }


}
