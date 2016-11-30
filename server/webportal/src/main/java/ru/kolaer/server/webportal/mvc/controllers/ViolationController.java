package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.JournalViolationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.TypeViolationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ViolationService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/violations")
@Api(tags = "Нарушения")
public class ViolationController {

    @Autowired
    private ViolationService violationService;

    @Autowired
    private TypeViolationService typeViolationService;

    @Autowired
    private JournalViolationService journalViolationService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(
            value = "Добавить журнал для нарушений",
            notes = "Добавить журнал для нарушений"
    )
    @UrlDeclaration(description = "Добавить журнал для нарушений", isAccessUser = true)
    @RequestMapping(value = "/journal/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody JournalViolation violation) {
        this.journalViolationService.add(new JournalViolationDecorator(violation));
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
            value = "Добавить нарушение в общий журнал",
            notes = "Добавить нарушение в общий журнал"
    )
    @UrlDeclaration(description = "Добавить нарушение в общий журнал", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addViolation(@ApiParam(value = "Нарушение") @RequestBody Violation violation) {
        violation.setWriter(serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity());

        JournalViolation generalJournalViolation = this.journalViolationService.getGeneralJournalViolation();
        generalJournalViolation.getViolations().add(new ViolationDecorator(violation));

        this.journalViolationService.update(generalJournalViolation);
    }

    @ApiOperation(
            value = "Добавить нарушение в журнал",
            notes = "Добавить нарушение в журнал"
    )
    @UrlDeclaration(description = "Добавить нарушение в журнал", isAccessUser = true)
    @RequestMapping(value = "/add/journal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addViolation(@ApiParam(value = "Журнал с нарушением") @RequestBody JournalViolation journalViolation) {
        GeneralEmployeesEntity generalEmployeesEntity = serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity();

        JournalViolation generalJournalViolation = this.journalViolationService.getById(journalViolation.getId());
        generalJournalViolation.getViolations().addAll(journalViolation.getViolations().stream().map(v -> {
            v.setWriter(generalEmployeesEntity);
            return new ViolationDecorator(v);
        }).collect(Collectors.toList()));

        this.journalViolationService.update(generalJournalViolation);
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
    public List<Violation> getViolations(@ApiParam(value = "ID журнала") @RequestBody JournalViolation journalViolation) {
        return this.journalViolationService.getById(journalViolation.getId()).getViolations();
    }

}
