package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.entities.japc.*;
import ru.kolaer.server.webportal.mvc.model.servirces.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/violations")
@Api(tags = "Нарушения")
public class ViolationController {
    private static final String ADMIN_VIOLATION = "Администратор нарушений";

    @Autowired
    private DepartamentService departamentService;

    @Autowired
    private ViolationService violationService;

    @Autowired
    private TypeViolationService typeViolationService;

    @Autowired
    private JournalViolationService journalViolationService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @Autowired
    private UrlPathService urlPathService;

    @Autowired
    private EmployeeService employeeService;


    @ApiOperation("Получить состояние")
    @UrlDeclaration(description = "Получить состояние", isAccessUser = true)
    @RequestMapping(value = "/stage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StageEnum[] getStage() {
        return StageEnum.values();
    }

    @ApiOperation("Получить доступы журналов")
    @UrlDeclaration(description = "Добавить журнал для нарушений", isAccessUser  = true)
    @RequestMapping(value = "/journal/access", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JournalViolationAccess getJournalAccess() {
        final GeneralAccountsEntity account = this.serviceLDAP.getAccountByAuthentication();
        final List<String> userRoles = account.getRoles().stream()
                .map(GeneralRolesEntity::getType).collect(Collectors.toList());
        final WebPortalUrlPath pathByUrl = urlPathService.getPathByUrl("/violations/journal/get/all");

        final boolean gettingAll = pathByUrl.getAccesses().contains("ALL") || pathByUrl.getAccesses().stream()
                .filter(userRoles::contains).count() > 0;

        final JournalViolationAccess access = new JournalViolationAccess();
        access.setGetAll(gettingAll);

        if(gettingAll) {
            final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains("OIT");

            List<JournalViolation> journalViolations = isAdmin
                    ? this.journalViolationService.getAll()
                    : this.journalViolationService.getAllByDep(account.getGeneralEmployeesEntity()
                    .getDepartament().getId());

            access.setJournalAccesses(journalViolations.stream().map(journalViolation -> {
                final JournalAccess journalAccess = new JournalAccess();
                journalAccess.setIdJournal(journalViolation.getId());
                journalAccess.setEdit(isAdmin);
                journalAccess.setRemove(isAdmin);
                return journalAccess;
            }).collect(Collectors.toList()));
        }

        return access;
    }

    @ApiOperation("Получить доступы для нарушений в журнале")
    @UrlDeclaration(description = "Получить доступы для нарушений в журнале", isAccessUser  = true)
    @RequestMapping(value = "/access", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ViolationAccess> getViolationAccess(@ApiParam("ID журнала") @RequestParam("id") Integer id) {
        JournalViolation journalViolation = this.journalViolationService.getById(id);

        if(journalViolation != null) {
            final GeneralAccountsEntity account = this.serviceLDAP.getAccountByAuthentication();
            final List<String> userRoles = account.getRoles().stream()
                    .map(GeneralRolesEntity::getType).collect(Collectors.toList());

            final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains("OIT");
            return journalViolation.getViolations().stream().map(violation -> {
                ViolationAccess violationAccess = new ViolationAccess();
                violationAccess.setId(violation.getId());
                violationAccess.setEdit(isAdmin);
                violationAccess.setDelete(isAdmin);
                return violationAccess;
            }).collect(Collectors.toList());
        }


        return Collections.emptyList();
    }

    @ApiOperation(
            value = "Добавить журнал для нарушений",
            notes = "Добавить журнал для нарушений"
    )
    @UrlDeclaration(description = "Добавить журнал для нарушений", isAccessUser = true)
    @RequestMapping(value = "/journal/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JournalViolation addJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody JournalViolation violation) {
        if(violation.getName()== null)
            throw new BadRequestException("Имя не может быть пустым!");

        JournalViolation journalViolation = new JournalViolationDecorator(violation);
        if(journalViolation.getDepartament() == null) {
            journalViolation.setDepartament(this.serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity().getDepartament());
        } else {
            journalViolation.setDepartament(departamentService.getById(journalViolation.getDepartament().getId()));
        }

        this.journalViolationService.add(journalViolation);
        return journalViolation;
    }

    @ApiOperation(
            value = "Удалить нарушение",
            notes = "Удалить нарушение"
    )
    @UrlDeclaration(description = "Удалить нарушения", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteViolations(@ApiParam(value = "Журнал нарушения") @RequestBody List<Violation> violations) {
        violations.forEach(this.violationService::delete);
    }

    @ApiOperation(
            value = "Удалить журнал для нарушений",
            notes = "Удалить журнал для нарушений"
    )
    @UrlDeclaration(description = "Удалить журнал для нарушений", isAccessUser = true)
    @RequestMapping(value = "/journal/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody List<JournalViolation> journalViolations) {
        journalViolations.forEach(journalViolation -> {
            JournalViolation journal = this.journalViolationService.getById(journalViolation.getId());
            this.deleteViolations(journal.getViolations());
            this.journalViolationService.delete(journal);
        });

    }

    @ApiOperation(
            value = "Получить все журналы с нарушениями",
            notes = "Получить все журналы с нарушениями"
    )
    @UrlDeclaration(description = "Получить все журналы с нарушениями", isAccessUser = true)
    @RequestMapping(value = "/journal/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<JournalViolation> getAllJournal() {
        final GeneralAccountsEntity account = this.serviceLDAP.getAccountByAuthentication();
        final List<String> userRoles = account.getRoles().stream()
                .map(GeneralRolesEntity::getType).collect(Collectors.toList());

        final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains("OIT");

        return isAdmin ? this.journalViolationService.getAll()
                : this.journalViolationService.getAllByDep(account.getGeneralEmployeesEntity()
                .getDepartament().getId());
    }

    @ApiOperation(
            value = "Добавить нарушение в журнал",
            notes = "Добавить нарушение в журнал"
    )
    @UrlDeclaration(description = "Добавить нарушение в журнал", isAccessUser = true)
    @RequestMapping(value = "/add/journal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Violation addViolation(@ApiParam(value = "Журнал с нарушением") @RequestBody JournalViolation journalViolation) {
        GeneralEmployeesEntity generalEmployeesEntity = serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity();

        JournalViolation generalJournalViolation = this.journalViolationService.getById(journalViolation.getId());
        if(generalJournalViolation == null)
            throw new BadRequestException("Не найден журнал с ID: " + journalViolation.getId());

        List<Violation> violations = Optional.ofNullable(generalJournalViolation.getViolations())
                .orElse(new ArrayList<>());

        Violation lastAdd = null;
        if(journalViolation.getViolations().size() == 1) {
            final Violation v = journalViolation.getViolations().get(0);
            if (v.getTypeViolation() != null)
                v.setTypeViolation(this.typeViolationService.getById(v.getTypeViolation().getId()));
            v.setWriter(generalEmployeesEntity);
            v.setStartMakingViolation(new Date());
            if (v.getExecutor() != null)
                v.setExecutor(this.employeeService.getById(v.getExecutor().getPnumber()));
            lastAdd = new ViolationDecorator(v);
            violations.add(lastAdd);
            generalJournalViolation.setViolations(violations);

            this.journalViolationService.update(generalJournalViolation);
        }

        return lastAdd;
    }

    @ApiOperation(
            value = "Получить все типы нарушений",
            notes = "Получить все типы нарушений"
    )
    @UrlDeclaration(description = "Получить все типы нарушений", isAccessUser = true)
    @RequestMapping(value = "/type/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TypeViolation> getAllTypes() {
        return this.typeViolationService.getAll();
    }

    @ApiOperation(
            value = "Получить все нарушения в журнале",
            notes = "Получить все нарушения в журнале"
    )
    @UrlDeclaration(description = "Получить все нарушения в журнале", isAccessUser = true)
    @RequestMapping(value = "/journal/violation/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Violation> getViolations(@ApiParam(value = "ID журнала") @RequestParam("id") Integer id) {
        return this.journalViolationService.getById(id).getViolations();
    }

}
