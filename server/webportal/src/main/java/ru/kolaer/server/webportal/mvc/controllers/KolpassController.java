package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.servirces.RepositoryPasswordHistoryService;
import ru.kolaer.server.webportal.mvc.model.servirces.RepositoryPasswordService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@RestController
@RequestMapping(value = "/kolpass")
@Api("Хранилища паролей (Парольница)")
@Slf4j
public class KolpassController {

    private static final String ADMIN = "OIT";

    @Autowired
    private RepositoryPasswordService repPassService;

    @Autowired
    private RepositoryPasswordHistoryService repPassHistoryService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(value = "Получить все хранилища")
    @UrlDeclaration(description = "Получить все хранилища")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RepositoryPassword> getAllRepositoryPasswords() {
        return this.repPassService.getAll();
    }

    @ApiOperation(value = "Получить все свои хранилища")
    @UrlDeclaration(description = "Получить все свои хранилища", isAccessUser = true)
    @RequestMapping(value = "/get/all/personal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<RepositoryPassword> getAllPersonalRepositoryPasswords(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize
    ) {
        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        return this.repPassService.getAllByPnumber(employeeEntity.getPersonnelNumber(), number, pageSize);
    }

    @ApiOperation(value = "Добавить новое хранилище")
    @UrlDeclaration(description = "Добавить новое хранилище", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPassword addRepositoryPasswords(
            @ApiParam("Наименование хранилища") @RequestBody RepositoryPassword repositoryPassword
    ) {
        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        if(repositoryPassword.getName() == null) {
            throw new BadRequestException("Имя не может быть пустым!");
        }

        final RepositoryPassword doubleRep = this.repPassService
                .getByNameAndPnumber(repositoryPassword.getName(), employeeEntity.getPersonnelNumber());

        if(doubleRep != null)
            throw new BadRequestException("Хранилище с таким названием у вас уже существует!");

        repositoryPassword.setEmployee(employeeEntity);

        this.repPassService.add(repositoryPassword);

        return repositoryPassword;
    }

    @ApiOperation(value = "Добавить новый пароль в хранилище")
    @UrlDeclaration(description = "Добавить новый пароль в хранилище", isAccessUser = true)
    @RequestMapping(value = "/passwords/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPassword addRepositoryPasswordHistory(
            @ApiParam("Хринилище и пароль (Пароль записывать в lastPassword!)") @RequestBody RepositoryPassword repositoryPassword
    ) {
        if(repositoryPassword.getId() == null) {
            throw new BadRequestException("ID не может быть пустым!");
        }

        if(repositoryPassword.getLastPassword() == null
                || repositoryPassword.getLastPassword().getPassword() == null) {
            throw new BadRequestException("Добавьте пароль!");
        }

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPassword rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.getRoles().stream()
                .filter(role -> role.getType().equals(ADMIN)).findFirst().isPresent()) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

        final RepositoryPasswordHistory lastPassword = repositoryPassword.getLastPassword();
        lastPassword.setPasswordWriteDate(new Date());
        lastPassword.setRepositoryPassword(rep);

        if(rep.getFirstPassword() == null) {
            rep.setFirstPassword(lastPassword);
        }

        if(rep.getPrevPassword() == null) {
            rep.setPrevPassword(lastPassword);
        } else {
            rep.setPrevPassword(rep.getLastPassword());
        }

        rep.setLastPassword(lastPassword);

        this.repPassService.update(rep);
        rep.getFirstPassword().setRepositoryPassword(null);
        rep.getPrevPassword().setRepositoryPassword(null);
        rep.getLastPassword().setRepositoryPassword(null);
        return rep;
    }

    @ApiOperation(value = "Удалить хранилище")
    @UrlDeclaration(description = "Удалить хранилище", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody RepositoryPassword repositoryPassword
    ) {
        if(repositoryPassword.getId() == null) {
            throw new BadRequestException("ID не может быть пустым!");
        }

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPassword rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.getRoles().stream()
                .filter(role -> role.getType().equals(ADMIN)).findFirst().isPresent()) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

        this.repPassHistoryService.delete(rep.getHistoryPasswords());
        this.repPassService.delete(rep);

        return ResponseEntity.ok("Операция успешно выполнена!");
    }

    @ApiOperation(value = "Очистить хранилище")
    @UrlDeclaration(description = "Очистить хранилище", isAccessUser = true)
    @RequestMapping(value = "/clear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPassword clearRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody RepositoryPassword repositoryPassword
    ) {
        if(repositoryPassword.getId() == null) {
            throw new BadRequestException("ID не может быть пустым!");
        }

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPassword rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.getRoles().stream()
                .filter(role -> role.getType().equals(ADMIN)).findFirst().isPresent()) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
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
    @UrlDeclaration(description = "Обновить хранилище", isAccessUser = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPassword updateRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody RepositoryPassword repositoryPassword
    ) {
        if(repositoryPassword.getId() == null)
            throw new BadRequestException("ID не может быть пустым!");

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPassword rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.getRoles().stream()
                .filter(role -> role.getType().equals(ADMIN)).findFirst().isPresent()) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

        final RepositoryPassword updateRep = this.repPassService
                .getByNameAndPnumber(repositoryPassword.getName(), employeeEntity.getPersonnelNumber());

        if(updateRep != null)
            throw new BadRequestException("Это имя уже занято!");

        rep.setName(repositoryPassword.getName());

        this.repPassService.update(rep);

        return rep;
    }

    @ApiOperation(value = "Получить историю хранилища")
    @UrlDeclaration(description = "Получить историю хранилища", isAccessUser = true)
    @RequestMapping(value = "/history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<RepositoryPasswordHistory> getRepositoryPasswordHistory(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
            @ApiParam("ID Хринилища") @RequestParam("id") Integer id
    ) {
        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPassword rep = this.repPassService.getRepositoryWithJoinById(id);

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || !accountByAuthentication.getRoles().stream()
                .filter(role -> role.getType().equals(ADMIN)).findFirst().isPresent()) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

        return this.repPassHistoryService.getHistoryByIdRepository(rep.getId(), number, pageSize);
    }

}
