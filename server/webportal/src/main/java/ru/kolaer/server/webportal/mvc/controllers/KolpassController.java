package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordHistoryService;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordRepositoryService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.01.2017.
 */
@RestController
@RequestMapping(value = "/kolpass")
@Api("Хранилища паролей (Парольница)")
@Slf4j
public class KolpassController {
    private PasswordRepositoryService repPassService;
    private PasswordHistoryService repPassHistoryService;
    private AuthenticationService authenticationService;
    private DepartmentService departmentService;

    public KolpassController(PasswordRepositoryService repPassService,
                             PasswordHistoryService repPassHistoryService,
                             AuthenticationService authenticationService,
                             DepartmentService departmentService) {
        this.repPassService = repPassService;
        this.repPassHistoryService = repPassHistoryService;
        this.authenticationService = authenticationService;
        this.departmentService = departmentService;
    }

    @ApiOperation(value = "Получить все хранилища")
    @UrlDeclaration(description = "Получить все хранилища")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PasswordRepositoryDto> getAllRepositoryPasswords() {
            return this.repPassService.getAll();
    }

    @ApiOperation(value = "Получить все хранилища начальников")
    @UrlDeclaration(description = "Получить все хранилища начальников")
    @RequestMapping(value = "/get/all/chief", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PasswordRepositoryDto> getAllRepositoryPasswordsChief() {
        List<Long> idsChief = this.departmentService.getAll().stream()
                .map(DepartmentDto::getChiefId)
                .filter(Objects::nonNull)
                .filter(pNumber -> !pNumber.equals(authenticationService.getAccountByAuthentication()
                        .getEmployee().getPersonnelNumber()))
                .collect(Collectors.toList());
        return repPassService.getAllByPnumbers(idsChief);
    }

    @ApiOperation(value = "Получить все свои хранилища")
    @UrlDeclaration(description = "Получить все свои хранилища")
    @RequestMapping(value = "/get/all/personal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordRepositoryDto> getAllPersonalRepositoryPasswords(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        EmployeeDto employeeEntity = accountByAuthentication.getEmployee();
        return repPassService.getAllByPnumber(employeeEntity.getPersonnelNumber(), number, pageSize);
    }

    @ApiOperation(value = "Добавить новое хранилище другому сотруднику")
    @UrlDeclaration(description = "Добавить новое хранилище другому сотруднику")
    @RequestMapping(value = "/add/employee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto addRepositoryPasswordsWithEmployee(
            @ApiParam("Наименование хранилища") @RequestBody PasswordRepositoryDto repositoryPassword) {
        if(repositoryPassword.getName() == null) {
            throw new UnexpectedRequestParams("Имя не может быть пустым!");
        }

        if(repositoryPassword.getEmployee() == null)
            throw new UnexpectedRequestParams("Не указан сотрудник!");

        repositoryPassword.setEmployee(repositoryPassword.getEmployee());

        this.repPassService.save(repositoryPassword);

        return repositoryPassword;
    }

    @ApiOperation(value = "Добавить новое хранилище")
    @UrlDeclaration(description = "Добавить новое хранилище")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto addRepositoryPasswords(
            @ApiParam("Наименование хранилища") @RequestBody PasswordRepositoryDto repositoryPassword) {
        repositoryPassword.setEmployee(authenticationService.getAccountByAuthentication().getEmployee());
        return this.addRepositoryPasswordsWithEmployee(repositoryPassword);
    }

    @ApiOperation(value = "Добавить новый пароль в хранилище")
    @UrlDeclaration(description = "Добавить новый пароль в хранилище")
    @RequestMapping(value = "/passwords/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto addRepositoryPasswordHistory(
            @ApiParam("Хринилище и пароль (Пароль записывать в lastPassword!)") @RequestBody PasswordRepositoryDto repositoryPassword) {
        if(repositoryPassword.getId() == null) {
            throw new UnexpectedRequestParams("ID не может быть пустым!");
        }

        if(repositoryPassword.getLastPassword() == null
                || repositoryPassword.getLastPassword().getLogin() == null) {
            throw new UnexpectedRequestParams("Добавьте логин!");
        }

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        EmployeeDto employeeEntity = accountByAuthentication.getEmployee();
        PasswordRepositoryDto rep = this.repPassService.getById(repositoryPassword.getId());

        if(rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || accountByAuthentication.isAccessOit()) {

            PasswordHistoryDto lastPassword = repositoryPassword.getLastPassword();
            lastPassword.setPasswordWriteDate(new Date());
            lastPassword.setRepositoryPasswordId(rep.getId());

            lastPassword = this.repPassHistoryService.save(lastPassword);

            if (rep.getFirstPassword() == null) {
                rep.setFirstPassword(lastPassword);
            }

            if (rep.getPrevPassword() == null) {
                rep.setPrevPassword(lastPassword);
            } else {
                rep.setPrevPassword(rep.getLastPassword());
            }

            rep.setLastPassword(lastPassword);

            this.repPassService.save(rep);

            return rep;
        }

        throw new CustomHttpCodeException("У вас нет доступа к хранилищу!",
                ErrorCode.FORBIDDEN,
                HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Удалить хранилище")
    @UrlDeclaration(description = "Удалить хранилище")
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> deleteRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody PasswordRepositoryDto repositoryPassword) {
        if(repositoryPassword.getId() == null) {
            throw new UnexpectedRequestParams("ID не может быть пустым!");
        }

        final AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        final EmployeeDto employeeEntity = accountByAuthentication.getEmployee();
        final PasswordRepositoryDto rep = this.repPassService.getById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.isAccessOit()) {
            throw new CustomHttpCodeException("У вас нет доступа к хранилищу!",
                    ErrorCode.FORBIDDEN,
                    HttpStatus.FORBIDDEN);
        }

        this.repPassHistoryService.deleteByIdRep(rep.getId());
        this.repPassService.delete(rep);

        return ResponseEntity.ok("Операция успешно выполнена!");
    }

    @ApiOperation(value = "Очистить хранилище")
    @UrlDeclaration(description = "Очистить хранилище")
    @RequestMapping(value = "/clear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto clearRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody PasswordRepositoryDto repositoryPassword) {
        if(repositoryPassword.getId() == null) {
            throw new UnexpectedRequestParams("ID не может быть пустым!");
        }

        final AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        final EmployeeDto employeeEntity = accountByAuthentication.getEmployee();
        final PasswordRepositoryDto rep = this.repPassService.getById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.isAccessOit()) {
            throw new CustomHttpCodeException("У вас нет доступа к хранилищу!",
                    ErrorCode.FORBIDDEN,
                    HttpStatus.FORBIDDEN);
        }

        this.repPassHistoryService.delete(rep.getHistoryPasswords());
        rep.getHistoryPasswords().clear();
        rep.setHistoryPasswords(null);
        rep.setLastPassword(null);
        rep.setPrevPassword(null);
        rep.setFirstPassword(null);

        return rep;
    }

    @ApiOperation(value = "Обновить хранилище")
    @UrlDeclaration(description = "Обновить хранилище")
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PasswordRepositoryDto updateRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody PasswordRepositoryDto repositoryPassword) {
        if(repositoryPassword.getId() == null) {
            throw new UnexpectedRequestParams("ID не может быть пустым!");
        }

        final AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        final EmployeeDto employeeEntity = accountByAuthentication.getEmployee();
        final PasswordRepositoryDto rep = this.repPassService.getById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.isAccessOit()) {
            throw new CustomHttpCodeException("У вас нет доступа к хранилищу!",
                    ErrorCode.FORBIDDEN,
                    HttpStatus.FORBIDDEN);
        }

        rep.setName(repositoryPassword.getName());

        this.repPassService.save(rep);

        return rep;
    }

    @ApiOperation(value = "Получить историю хранилища")
    @UrlDeclaration(description = "Получить историю хранилища")
    @RequestMapping(value = "/history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PasswordHistoryDto> getRepositoryPasswordHistory(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            @ApiParam("ID Хринилища") @RequestParam("id") Long id) {
        final AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        final EmployeeDto employeeEntity = accountByAuthentication.getEmployee();
        final PasswordRepositoryDto rep = this.repPassService.getById(id);

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.isAccessOit()) {
            throw new CustomHttpCodeException("У вас нет доступа к хранилищу!",
                    ErrorCode.FORBIDDEN,
                    HttpStatus.FORBIDDEN);
        }

        return this.repPassHistoryService.getHistoryByIdRepository(rep.getId(), number, pageSize);
    }
}
