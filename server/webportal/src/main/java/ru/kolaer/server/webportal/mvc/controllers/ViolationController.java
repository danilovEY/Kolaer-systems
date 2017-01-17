package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/violations")
@Api(tags = "Нарушения")
@Slf4j
public class ViolationController extends BaseController {
    private static final String ADMIN_VIOLATION = "Администратор нарушений";
    private static final String ADMIN = "OIT";
    private static final String COURATOR_VIOLATION = "Куратор нарушений";

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

        final WebPortalUrlPath pathByUrlGetAll = urlPathService.getPathByUrl("/violations/journal/get/all");
        final WebPortalUrlPath pathByUrlAddJournal = urlPathService.getPathByUrl("/violations/journal/add");
        final boolean gettingAll = pathByUrlGetAll.getAccesses().contains("ALL") || pathByUrlGetAll.getAccesses().stream()
                .anyMatch(userRoles::contains);

        final boolean addJournal = pathByUrlAddJournal.getAccesses().contains("ALL") || pathByUrlAddJournal.getAccesses().stream()
                .anyMatch(userRoles::contains);

        final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains(ADMIN);

        final JournalViolationAccess access = new JournalViolationAccess();
        access.setGetAll(gettingAll);
        access.setAddJournal(addJournal);
        access.setAddAnyJournal(isAdmin);

        if(gettingAll) {
            List<JournalViolation> journalViolations;
            if(isAdmin) {
                journalViolations = this.journalViolationService.getAll();
            } else {
                journalViolations = userRoles.contains(COURATOR_VIOLATION) ? this.journalViolationService
                        .getAllByDep(account.getGeneralEmployeesEntity().getDepartament().getId()).getData()
                        : this.journalViolationService.getByPnumberWriter(account.getGeneralEmployeesEntity().getPnumber()).getData();
            }

            access.setJournalAccesses(journalViolations.stream().map(journalViolation -> {
                final JournalAccess journalAccess = new JournalAccess();
                journalAccess.setIdJournal(journalViolation.getId());
                journalAccess.setEdit(isAdmin);
                journalAccess.setDelete(isAdmin);
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

            final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains(ADMIN);

            return this.violationService.getByIdJournal(journalViolation.getId()).getData().stream().map(violation -> {
                ViolationAccess violationAccess = new ViolationAccess();
                violationAccess.setId(violation.getId());
                violationAccess.setEdit(isAdmin || account.getGeneralEmployeesEntity().getPnumber()
                        .equals(violation.getWriter().getPnumber()));
                violationAccess.setDelete(isAdmin);
                violationAccess.setEffective(isAdmin);
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

        final GeneralAccountsEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();

        if(accountByAuthentication.getRoles().stream().map(GeneralRolesEntity::getType).filter(role -> !ADMIN_VIOLATION.equals(role)
                || !ADMIN.equals(role) || !COURATOR_VIOLATION.equals(role)).findFirst().isPresent()) {
            if(this.journalViolationService.getCountByPnumberWriter(accountByAuthentication
                    .getGeneralEmployeesEntity().getPnumber()) > 0) {
                throw new AccessDeniedException("Вы не можете больше создавать журналы!");
            }
        }

        JournalViolation journalViolation = new JournalViolationDecorator(violation);
        journalViolation.setWriter(accountByAuthentication.getGeneralEmployeesEntity());
        if(journalViolation.getDepartament() == null) {
            journalViolation.setDepartament(accountByAuthentication.getGeneralEmployeesEntity().getDepartament());
        } else {
            journalViolation.setDepartament(departamentService.getById(journalViolation.getDepartament().getId()));
        }

        this.journalViolationService.add(journalViolation);
        return journalViolation;
    }

    @ApiOperation(
            value = "Обновить журнал для нарушений",
            notes = "Обновить журнал для нарушений"
    )
    @UrlDeclaration(description = "Обновить журнал для нарушений", isAccessUser = true)
    @RequestMapping(value = "/journal/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JournalViolation updateJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody JournalViolation violation) {
        JournalViolation journalViolation = this.journalViolationService.getById(violation.getId());
        if(violation.getDepartament() != null) {
            journalViolation.setDepartament(departamentService.getById(violation.getDepartament().getId()));
        }

        if(violation.getName() != null) {
            journalViolation.setName(violation.getName());
        }

        this.journalViolationService.update(journalViolation);
        return journalViolation;
    }

    @ApiOperation(
            value = "Удалить нарушение",
            notes = "Удалить нарушение"
    )
    @UrlDeclaration(description = "Удалить нарушения", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteViolations(@ApiParam(value = "Журнал нарушения") @RequestBody List<Violation> violations) {
        this.violationService.delete(violations.stream().filter(violation -> violation.getId() != null)
                .map(ViolationDecorator::new).collect(Collectors.toList()));
    }

    @ApiOperation(
            value = "Удалить журнал для нарушений",
            notes = "Удалить журнал для нарушений"
    )
    @UrlDeclaration(description = "Удалить журнал для нарушений", isAccessUser = true)
    @RequestMapping(value = "/journal/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody List<JournalViolation> journalViolations) {
        journalViolations.stream().filter(journalViolation -> journalViolation.getId() != null)
                .forEach(journalViolation -> {
                    JournalViolation journal = this.journalViolationService.getById(journalViolation.getId());
                    this.violationService.deleteByJournalId(journal.getId());
                    this.journalViolationService.delete(journal);
        });

    }

    @ApiOperation(
            value = "Получить все журналы с нарушениями",
            notes = "Получить все журналы с нарушениями"
    )
    @UrlDeclaration(description = "Получить все журналы с нарушениями", isAccessUser = true)
    @RequestMapping(value = "/journal/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<JournalViolation> getAllJournal(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        final GeneralAccountsEntity account = this.serviceLDAP.getAccountByAuthentication();
        final List<String> userRoles = account.getRoles().stream()
                .map(GeneralRolesEntity::getType).collect(Collectors.toList());

        final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains(ADMIN);

        if(isAdmin) {
            return this.journalViolationService.getAllJournal(number, pageSize);
        } else {
            final boolean isCourator = userRoles.contains(COURATOR_VIOLATION);

            return isCourator
                    ? this.journalViolationService.getAllByDep(account.getGeneralEmployeesEntity()
                    .getDepartament().getId(), number, pageSize)
                    : this.journalViolationService.getByPnumberWriter(account.getGeneralEmployeesEntity()
                    .getPnumber(), number, pageSize);
        }
    }

    @ApiOperation(
            value = "Обновить нарушение в журнал",
            notes = "Обновить нарушение в журнал"
    )
    @UrlDeclaration(description = "Обновить нарушение в журнал", isAccessUser = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Violation updateViolation(@ApiParam(value = "Нарушением") @RequestBody Violation violation) {
        Violation updateViolation = this.violationService.getById(violation.getId());
        if(updateViolation == null)
            throw new BadRequestException("Не найден журнал с ID: " + violation.getId());

        if(violation.getTypeViolation() != null) {
            updateViolation.setTypeViolation(this.typeViolationService.getById(violation.getTypeViolation().getId()));
        }

        if(violation.getExecutor() != null) {
            updateViolation.setExecutor(this.employeeService.getById(violation.getExecutor().getPnumber()));
        }

        if(violation.isEffective() != null) {
            updateViolation.setEffective(violation.isEffective());
            if(violation.isEffective()) {
                updateViolation.setDateEndEliminationViolation(new Date());
            }
        }

        if(violation.getStageEnum() != null) {
            updateViolation.setStageEnum(violation.getStageEnum());
        }

        if(violation.getTodo() != null) {
            updateViolation.setTodo(violation.getTodo());
        }

        if(violation.getViolation() != null) {
            updateViolation.setViolation(violation.getViolation());
        }

        updateViolation.setDateLimitEliminationViolation(violation.getDateLimitEliminationViolation());

        this.violationService.update(updateViolation);

        return updateViolation;
    }

    @ApiOperation(
            value = "Обновить нарушение в журнал",
            notes = "Обновить нарушение в журнал"
    )
    @UrlDeclaration(description = "Обновить нарушение в журнал", isAccessUser = true)
    @RequestMapping(value = "/update/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Violation> updateEffectiveViolation(@ApiParam(value = "Нарушением") @RequestBody List<Violation> violations) {
        return violations.stream().map(violation -> {
            Violation updateViolation = this.violationService.getById(violation.getId());
            if (updateViolation == null)
                throw new BadRequestException("Не найден журнал с ID: " + violation.getId());

            if(violation.getTypeViolation() != null) {
                updateViolation.setTypeViolation(this.typeViolationService.getById(violation.getTypeViolation().getId()));
            }

            if(violation.getExecutor() != null) {
                updateViolation.setExecutor(this.employeeService.getById(violation.getExecutor().getPnumber()));
            }

            if(violation.isEffective() != null) {
                updateViolation.setEffective(violation.isEffective());
                if(violation.isEffective()) {
                    updateViolation.setDateEndEliminationViolation(new Date());
                }
            }

            if(violation.getStageEnum() != null) {
                updateViolation.setStageEnum(violation.getStageEnum());
            }

            if(violation.getTodo() != null) {
                updateViolation.setTodo(violation.getTodo());
            }

            if(violation.getViolation() != null) {
                updateViolation.setViolation(violation.getViolation());
            }

            if(violation.getDateLimitEliminationViolation() != null)
                updateViolation.setDateLimitEliminationViolation(violation.getDateLimitEliminationViolation());

            this.violationService.update(updateViolation);

            return updateViolation;
        }).collect(Collectors.toList());
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

        List<Violation> violations = Optional
                .ofNullable(this.violationService.getByIdJournal(generalJournalViolation.getId()).getData())
                .orElse(new ArrayList<>());

        Violation lastAdd = null;
        if(journalViolation.getViolations().size() == 1) {
            final Violation v = journalViolation.getViolations().get(0);
            if (v.getTypeViolation() != null)
                v.setTypeViolation(this.typeViolationService.getById(v.getTypeViolation().getId()));
            v.setWriter(generalEmployeesEntity);
            v.setStartMakingViolation(new Date());
            v.setEffective(false);
            if (v.getExecutor() != null)
                v.setExecutor(this.employeeService.getById(v.getExecutor().getPnumber()));
            v.setJournalViolation(generalJournalViolation);
            lastAdd = new ViolationDecorator(v);
            violations.add(lastAdd);
            generalJournalViolation.setViolations(violations);

            this.journalViolationService.update(generalJournalViolation);
            lastAdd.setJournalViolation(null);
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
    public Page<Violation> getViolations(
            @ApiParam(value = "ID журнала") @RequestParam("id") Integer id,
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        final GeneralAccountsEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final GeneralEmployeesEntity generalEmployeesEntity = accountByAuthentication.getGeneralEmployeesEntity();
        final List<String> roleStream = this.serviceLDAP.getAccountByAuthentication().getRoles().stream()
                .map(GeneralRolesEntity::getType).collect(Collectors.toList());

        final boolean isAdmin = roleStream.contains(ADMIN_VIOLATION)
                        || roleStream.contains(ADMIN);

        if(isAdmin) {
            return this.violationService.getByIdJournal(id, number, pageSize);
        } else {
            final JournalViolation journal = this.journalViolationService.getById(id);
            if(journal.getWriter().getPnumber().equals(generalEmployeesEntity.getPnumber())
                    || (journal.getDepartament().getId().equals(generalEmployeesEntity.getDepartament().getId())
                    && roleStream.contains(COURATOR_VIOLATION))) {
                return this.violationService.getByIdJournal(id, number, pageSize);
            }

            throw new AccessDeniedException("У вас нет доступа!");
        }
    }
}
