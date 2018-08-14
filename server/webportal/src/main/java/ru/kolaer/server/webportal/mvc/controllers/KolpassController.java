package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.kolpass.RepositoryPasswordFilter;
import ru.kolaer.server.webportal.mvc.model.dto.kolpass.RepositoryPasswordSort;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordRepositoryService;

import java.util.List;

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
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            RepositoryPasswordSort sort,
            RepositoryPasswordFilter filter) {
        return passwordRepositoryService.getAll(sort, filter, number, pageSize);
    }

    @ApiOperation(value = "Получить хранилище")
    @UrlDeclaration(description = "Получить хранилище")
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto getRepositoryPasswords(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        return passwordRepositoryService.getById(repId);
    }

    @ApiOperation(value = "Получить всех расширенных пользователей")
    @UrlDeclaration(description = "Получить всех расширенных пользователей", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/rep/share", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordRepositoryDto> getAccountsFromSharePasswordRepository(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            RepositoryPasswordSort sort,
            RepositoryPasswordFilter filter) {
        return passwordRepositoryService.getAllShared(sort, filter, number, pageSize);
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

    @ApiOperation(value = "Расшарить пароль для пользователей")
    @UrlDeclaration(description = "Расшарить пароль для пользователей", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/rep/{repId}/share", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void sharePasswordRepository(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("ID пользователя") @RequestParam("accountId") Long accountId) {
        this.passwordRepositoryService.shareRepository(repId, accountId);
    }

    @ApiOperation(value = "Удалить пользователя из расшаривания")
    @UrlDeclaration(description = "Удалить пользователя из расшаривания", requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/rep/{repId}/share", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteSharePasswordRepository(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("ID пользователя") @RequestParam("accountId") Long accountId) {
        this.passwordRepositoryService.deleteAccountFromShare(repId, accountId);
    }

    @ApiOperation(value = "Получить всех расширенных пользователей")
    @UrlDeclaration(description = "Получить всех расширенных пользователей", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/rep/{repId}/share", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccountDto> getAccountsFromSharePasswordRepository(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        return this.passwordRepositoryService.getAllAccountFromShare(repId);
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
    @UrlDeclaration(description = "Обновить хранилище", requestMethod = RequestMethod.PUT)
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
