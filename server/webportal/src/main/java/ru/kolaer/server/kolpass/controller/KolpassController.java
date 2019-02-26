package ru.kolaer.server.kolpass.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.kolpass.model.request.RepositoryPasswordFilter;
import ru.kolaer.server.kolpass.model.request.RepositoryPasswordSort;
import ru.kolaer.server.kolpass.service.PasswordRepositoryService;

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
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordRepositoryDto> getAllPersonalRepositoryPasswords(
            @ApiParam("Номер страници") @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            RepositoryPasswordSort sort,
            RepositoryPasswordFilter filter) {
        return passwordRepositoryService.getAll(sort, filter, pageNum, pageSize);
    }

    @ApiOperation(value = "Получить хранилище")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto getRepositoryPasswords(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        return passwordRepositoryService.getById(repId);
    }

    @ApiOperation(value = "Получить всех расширенных пользователей")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/share", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordRepositoryDto> getAccountsFromSharePasswordRepository(
            @ApiParam("Номер страници") @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            RepositoryPasswordSort sort,
            RepositoryPasswordFilter filter) {
        return passwordRepositoryService.getAllShared(sort, filter, pageNum, pageSize);
    }

    @ApiOperation(value = "Добавить новое хранилище")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto addRepositoryPasswords(
            @ApiParam("Наименование хранилища") @RequestBody PasswordRepositoryDto repositoryPassword) {
        return passwordRepositoryService.add(repositoryPassword);
    }

    @ApiOperation(value = "Добавить новый пароль в хранилище")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}/passwords", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordHistoryDto addPasswordHistory(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("Пароль") @RequestBody PasswordHistoryDto passwordHistoryDto) {
        return this.passwordRepositoryService.addPassword(repId, passwordHistoryDto);
    }

    @ApiOperation(value = "Расшарить пароль для пользователей")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}/share", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void sharePasswordRepository(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("ID пользователя") @RequestParam("accountId") Long accountId) {
        this.passwordRepositoryService.shareRepository(repId, accountId);
    }

    @ApiOperation(value = "Удалить пользователя из расшаривания")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}/share", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteSharePasswordRepository(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("ID пользователя") @RequestParam("accountId") Long accountId) {
        this.passwordRepositoryService.deleteAccountFromShare(repId, accountId);
    }

    @ApiOperation(value = "Получить всех расширенных пользователей")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}/share", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccountDto> getAccountsFromSharePasswordRepository(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        return this.passwordRepositoryService.getAllAccountFromShare(repId);
    }

    @ApiOperation(value = "Удалить новый пароль в хранилище")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}/passwords/{passId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deletePasswordHistory(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("ID Пароля") @PathVariable("passId") Long passId) {
        this.passwordRepositoryService.deletePassword(repId, passId);
    }

    @ApiOperation(value = "Удалить хранилище")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRepositoryPassword(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        this.passwordRepositoryService.deleteByIdRep(repId);
    }

    @ApiOperation(value = "Обновить хранилище")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateRepositoryPassword(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
                                         @ApiParam("Наименование хранилища") @RequestBody PasswordRepositoryDto repositoryPassword) {
        repositoryPassword.setId(repId);
        this.passwordRepositoryService.update(repositoryPassword);
    }

    @ApiOperation(value = "Очистить хранилище")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/rep/{repId}/clear", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void clearRepositoryPassword(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId) {
        this.passwordRepositoryService.clearRepository(repId);
    }

    @ApiOperation(value = "Получить историю хранилища")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/rep/{repId}/passwords", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordHistoryDto> getRepositoryPasswordHistory(
            @ApiParam("ID Хринилища") @PathVariable("repId") Long repId,
            @ApiParam("Номер страници") @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam("Размер страници") @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        return this.passwordRepositoryService.getHistoryByIdRepository(repId, pageNum, pageSize);
    }

    @ApiOperation(value = "Получить последний логин хранилища")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/rep/{repId}/passwords/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordHistoryDto getLastHistoryInRepository(@ApiParam("ID Хринилища") @PathVariable("repId") Long repId){
        return this.passwordRepositoryService.getLastHistoryInRepository(repId);
    }
}
