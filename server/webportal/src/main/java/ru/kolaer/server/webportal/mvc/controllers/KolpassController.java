package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by danilovey on 20.01.2017.
 */
@RestController
@RequestMapping(value = "/kolpass")
@Api("Хранилища паролей (Парольница)")
@Slf4j
public class KolpassController {
/*
    private static final String ADMIN = "OIT";

    @Autowired
    private RepositoryPasswordService repPassService;

    @Autowired
    private RepositoryPasswordHistoryService repPassHistoryService;

    @Autowired
    private AuthenticationService serviceLDAP;

    @Autowired
    private DepartmentService departmentService;


    @ApiOperation(value = "Получить все хранилища")
    @UrlDeclaration(description = "Получить все хранилища")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RepositoryPasswordEntity> getAllRepositoryPasswords() {
            return this.repPassService.getAll();
    }

    @ApiOperation(value = "Получить все хранилища начальников")
    @UrlDeclaration(description = "Получить все хранилища начальников")
    @RequestMapping(value = "/get/all/chief", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RepositoryPasswordEntity> getAllRepositoryPasswordsChief() {
        List<Integer> idsChief = this.departmentService.getAll().stream()
                .map(DepartmentEntity::getChiefEntity)
                .filter(Objects::nonNull)
                .filter(pNumber -> !pNumber.equals(this.serviceLDAP.getAccountByAuthentication()
                        .getEmployeeEntity().getPersonnelNumber()))
                .collect(Collectors.toList());
        return this.repPassService.getAllByPnumbers(idsChief);
    }

    @ApiOperation(value = "Получить все свои хранилища")
    @UrlDeclaration(description = "Получить все свои хранилища", isAccessUser = true)
    @RequestMapping(value = "/get/all/personal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<RepositoryPasswordEntity> getAllPersonalRepositoryPasswords(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize
    ) {
        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        return this.repPassService.getAllByPnumber(employeeEntity.getPersonnelNumber(), number, pageSize);
    }

    @ApiOperation(value = "Добавить новое хранилище другому сотруднику")
    @UrlDeclaration(description = "Добавить новое хранилище другому сотруднику")
    @RequestMapping(value = "/add/employee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPasswordEntity addRepositoryPasswordsWithEmployee(
            @ApiParam("Наименование хранилища") @RequestBody RepositoryPasswordEntity repositoryPassword
    ) {
        if(repositoryPassword.getName() == null) {
            throw new BadRequestException("Имя не может быть пустым!");
        }

        if(repositoryPassword.getEmployee() == null)
            throw new BadRequestException("Не указан сотрудник!");

        repositoryPassword.setEmployee(new EmployeeEntity(repositoryPassword.getEmployee()));

        this.repPassService.add(new RepositoryPasswordEntity(repositoryPassword));

        return repositoryPassword;
    }

    @ApiOperation(value = "Добавить новое хранилище")
    @UrlDeclaration(description = "Добавить новое хранилище", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPasswordEntity addRepositoryPasswords(
            @ApiParam("Наименование хранилища") @RequestBody RepositoryPasswordEntity repositoryPassword
    ) {
        repositoryPassword.setEmployee(this.serviceLDAP.getAccountByAuthentication().getEmployeeEntity());
        return this.addRepositoryPasswordsWithEmployee(repositoryPassword);
    }

    @ApiOperation(value = "Добавить новый пароль в хранилище")
    @UrlDeclaration(description = "Добавить новый пароль в хранилище", isAccessUser = true)
    @RequestMapping(value = "/passwords/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPasswordEntity addRepositoryPasswordHistory(
            @ApiParam("Хринилище и пароль (Пароль записывать в lastPassword!)") @RequestBody RepositoryPasswordEntity repositoryPassword
    ) {
        if(repositoryPassword.getId() == null) {
            throw new BadRequestException("ID не может быть пустым!");
        }

        if(repositoryPassword.getLastPassword() == null
                || repositoryPassword.getLastPassword().getLogin() == null) {
            throw new BadRequestException("Добавьте логин!");
        }

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPasswordEntity rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || accountByAuthentication.getRoles().stream()
                .anyMatch(role -> role.getType().equals(ADMIN))) {

            final RepositoryPasswordHistory lastPassword =
                    new RepositoryPasswordHistoryEntity(repositoryPassword.getLastPassword());
            lastPassword.setPasswordWriteDate(new Date());
            lastPassword.setRepositoryPassword(rep);

            this.repPassHistoryService.add(lastPassword);

            if (rep.getFirstPassword() == null) {
                rep.setFirstPassword(lastPassword);
            }

            if (rep.getPrevPassword() == null) {
                rep.setPrevPassword(lastPassword);
            } else {
                rep.setPrevPassword(rep.getLastPassword());
            }

            rep.setLastPassword(lastPassword);

            this.repPassService.update(rep);

            return rep;
        }

        throw new AccessDeniedException("У вас нет доступа к хранилищу!");
    }

    @ApiOperation(value = "Удалить хранилище")
    @UrlDeclaration(description = "Удалить хранилище", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody RepositoryPasswordEntity repositoryPassword
    ) {
        if(repositoryPassword.getId() == null) {
            throw new BadRequestException("ID не может быть пустым!");
        }

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPasswordEntity rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || accountByAuthentication.getRoles().stream()
                .noneMatch(role -> role.getType().equals(ADMIN))) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

        this.repPassHistoryService.deleteByIdRep(rep.getId());
        this.repPassService.delete(rep);

        return ResponseEntity.ok("Операция успешно выполнена!");
    }

    @ApiOperation(value = "Очистить хранилище")
    @UrlDeclaration(description = "Очистить хранилище", isAccessUser = true)
    @RequestMapping(value = "/clear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RepositoryPasswordEntity clearRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody RepositoryPasswordEntity repositoryPassword
    ) {
        if(repositoryPassword.getId() == null) {
            throw new BadRequestException("ID не может быть пустым!");
        }

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPasswordEntity rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || accountByAuthentication.getRoles().stream()
                .noneMatch(role -> role.getType().equals(ADMIN))) {
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
    public RepositoryPasswordEntity updateRepositoryPassword(
            @ApiParam("Хринилище") @RequestBody RepositoryPasswordEntity repositoryPassword
    ) {
        if(repositoryPassword.getId() == null)
            throw new BadRequestException("ID не может быть пустым!");

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final RepositoryPasswordEntity rep = this.repPassService.getRepositoryWithJoinById(repositoryPassword.getId());

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || accountByAuthentication.getRoles().stream()
                .noneMatch(role -> role.getType().equals(ADMIN))) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

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
        final RepositoryPasswordEntity rep = this.repPassService.getRepositoryWithJoinById(id);

        if(!rep.getEmployee().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                || accountByAuthentication.getRoles().stream()
                .noneMatch(role -> role.getType().equals(ADMIN))) {
            throw new AccessDeniedException("У вас нет доступа к хранилищу!");
        }

        return this.repPassHistoryService.getHistoryByIdRepository(rep.getId(), number, pageSize);
    }
*/
}
