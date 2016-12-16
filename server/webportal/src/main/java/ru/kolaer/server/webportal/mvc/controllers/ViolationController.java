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
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalAccess;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationAccess;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.*;

import java.util.Date;
import java.util.List;
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


    @ApiOperation("Получить доступы журналов")
    @UrlDeclaration(description = "Добавить журнал для нарушений", isAccessAll = true)
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
                    .getDepartament().getName());

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
            value = "Получить все журналы с нарушениями",
            notes = "Получить все журналы с нарушениями"
    )
    @UrlDeclaration(description = "Получить все журналы с нарушениями", isAccessUser = true)
    @RequestMapping(value = "/journal/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<JournalViolation> getAllJournal() {
        return this.journalViolationService.getAll();
    }

    @ApiOperation(
            value = "Добавить нарушение в журнал",
            notes = "Добавить нарушение в журнал"
    )
    @UrlDeclaration(description = "Добавить нарушение в журнал", isAccessUser = true)
    @RequestMapping(value = "/add/journal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Violation> addViolation(@ApiParam(value = "Журнал с нарушением") @RequestBody JournalViolation journalViolation) {
        GeneralEmployeesEntity generalEmployeesEntity = serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity();

        JournalViolation generalJournalViolation = this.journalViolationService.getById(journalViolation.getId());
        generalJournalViolation.getViolations().addAll(journalViolation.getViolations().stream()
                .filter(violation ->
                    violation.getTypeViolation() != null
                ).map(v -> {
                    v.setTypeViolation(this.typeViolationService.getById(v.getTypeViolation().getId()));
                    v.setWriter(generalEmployeesEntity);
                    v.setStartMakingViolation(new Date());
                    return new ViolationDecorator(v);
        }).collect(Collectors.toList()));

        this.journalViolationService.update(generalJournalViolation);
        return generalJournalViolation.getViolations();
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
