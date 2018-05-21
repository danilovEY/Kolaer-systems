package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordRepositoryService;

/**
 * Created by danilovey on 20.01.2017.
 */
@RestController
@RequestMapping(value = "/kolpass")
@Api("Хранилища паролей (Парольница)")
@Slf4j
public class KolpassController {
    private PasswordRepositoryService passwordRepositoryService;

    public KolpassController(PasswordRepositoryService passwordRepositoryService) {
        this.passwordRepositoryService = passwordRepositoryService;
    }

    @ApiOperation(value = "Получить все свои хранилища")
    @UrlDeclaration(description = "Получить все свои хранилища")
    @RequestMapping(value = "/rep", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordRepositoryDto> getAllPersonalRepositoryPasswords(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return passwordRepositoryService.getAllAuthAccount(number, pageSize);
    }

    @ApiOperation(value = "Добавить новое хранилище")
    @UrlDeclaration(description = "Добавить новое хранилище", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/rep", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto addRepositoryPasswords(
            @ApiParam("Наименование хранилища") @RequestBody PasswordRepositoryDto repositoryPassword) {
        return passwordRepositoryService.add(repositoryPassword);
    }

    @ApiOperation(value = "Добавить новый пароль в хранилище")
    @UrlDeclaration(description = "Добавить новый пароль в хранилище", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/rep/{repId}/passwords", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordHistoryDto addPasswordHistory(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("Пароль") @RequestBody PasswordHistoryDto passwordHistoryDto) {
        return this.passwordRepositoryService.addPassword(repId, passwordHistoryDto);
    }

    @ApiOperation(value = "Удалить новый пароль в хранилище")
    @UrlDeclaration(description = "Удалить новый пароль в хранилище", requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/rep/{repId}/passwords/{passId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deletePasswordHistory(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("ID Пароля") @PathVariable("passId") Long passId) {
        this.passwordRepositoryService.deletePassword(repId, passId);
    }

    @ApiOperation(value = "Удалить хранилище")
    @UrlDeclaration(description = "Удалить хранилище", requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRepositoryPassword(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        this.passwordRepositoryService.deleteByIdRep(repId);
    }

    @ApiOperation(value = "Обновить хранилище")
    @UrlDeclaration(description = "Обновить хранилище", requestMethod = RequestMethod.PATCH)
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateRepositoryPassword(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
                                         @ApiParam("Наименование хранилища") @RequestBody PasswordRepositoryDto repositoryPassword) {
        repositoryPassword.setId(repId);
        this.passwordRepositoryService.update(repositoryPassword);
    }

    @ApiOperation(value = "Очистить хранилище")
    @UrlDeclaration(description = "Очистить хранилище", requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/rep/{repId}/clear", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void clearRepositoryPassword(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        this.passwordRepositoryService.clearRepository(repId);
    }

    @ApiOperation(value = "Получить историю хранилища")
    @UrlDeclaration(description = "Получить историю хранилища")
    @RequestMapping(value = "/rep/{repId}/passwords", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordHistoryDto> getRepositoryPasswordHistory(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return this.passwordRepositoryService.getHistoryByIdRepository(repId, number, pageSize);
    }

    @ApiOperation(value = "Получить последний логин хранилища")
    @UrlDeclaration(description = "Получить последний логин хранилища")
    @RequestMapping(value = "/rep/{repId}/passwords/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordHistoryDto getLastHistoryInRepository(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId){
        return this.passwordRepositoryService.getLastHistoryInRepository(repId);
    }
}
