package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.ViolationReport;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dto.JournalAccess;
import ru.kolaer.server.webportal.mvc.model.dto.JournalViolationAccess;
import ru.kolaer.server.webportal.mvc.model.dto.ViolationAccess;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/violations")
@Api(tags = "Нарушения")
@Slf4j
public class ViolationController extends BaseController {
    private static final String ADMIN_VIOLATION = "Администратор нарушений";
    private static final String ADMIN = "OIT";
    private static final String COURATOR_VIOLATION = "Куратор нарушений";

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ViolationService violationService;

    @Autowired
    private TypeViolationService typeViolationService;

    @Autowired
    private JournalViolationService journalViolationService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @Autowired
    private UrlSecurityService urlSecurityService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ViolationReport violationReport;


    @ApiOperation("Сгенерировать отчет по всем нарушениям")
    @UrlDeclaration(description = "Сгенерировать отчет по всем нарушениям", isAccessUser = true)
    @RequestMapping(value = "/reports/generate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity generateReportByAllViolations(
            @ApiParam("Генерировать по всем подразделениям") @RequestParam(value = "all", defaultValue = "false") Boolean all,
            @ApiParam("Генерировать подробную информацию") @RequestParam(value = "detailed", defaultValue = "false") Boolean detailed,
            @ApiParam("Генерировать по ID подразделению") @RequestParam(value = "idDep", defaultValue = "-1") Integer idDep,

            @ApiParam("Генерировать в промежутке С") @RequestParam(value = "createStart", defaultValue = "-1") Long startUnix,

            @ApiParam("Генерировать в промежутке ПО") @RequestParam(value = "createEnd", defaultValue = "-1") Long endUnix,

            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Date start = !startUnix.equals(-1L) ? new Date(startUnix * 1000) : null;
        final Date end = !endUnix.equals(-1L) ? new Date(endUnix * 1000) : null;

        final File report = all ? this.violationReport.makeReportStatisticByAllDepBetween(detailed, start, end)
                : this.violationReport.makeReportStatisticByDepBetween(idDep, detailed, start, end);

        if(report != null) {
            try (InputStream fileInputStream = new FileInputStream(report);
                 OutputStream output = response.getOutputStream()) {

                response.setContentType("attachment/ms-excel; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentLength((int) (report.length()));

                response.addHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder
                        .encode(report.getName(),"UTF-8").replace("+", "%20") + "\"");

                IOUtils.copyLarge(fileInputStream, output);
                output.flush();
            } catch (IOException e) {
                log.error("Ошибка при передачи файла!");
                throw e;
            }
        }
        return ResponseEntity.ok("");
    }

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
        final AccountEntity account = this.serviceLDAP.getAccountByAuthentication();
        final List<String> userRoles = account.getRoles().stream()
                .map(RoleEntity::getType).collect(Collectors.toList());

        final UrlSecurity pathByUrlGetAll = urlSecurityService.getPathByUrl("/violations/journal/get/all");
        final UrlSecurity pathByUrlAddJournal = urlSecurityService.getPathByUrl("/violations/journal/add");
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
            List<JournalViolationEntity> journalViolations;
            if(isAdmin) {
                journalViolations = this.journalViolationService.getAll();
            } else {
                journalViolations = userRoles.contains(COURATOR_VIOLATION) ? this.journalViolationService
                        .getAllByDep(account.getEmployeeEntity().getDepartment().getId()).getData()
                        : this.journalViolationService.getByPnumberWriter(account.getEmployeeEntity().getPersonnelNumber()).getData();
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
        JournalViolationEntity journalViolation = this.journalViolationService.getById(id);

        if(journalViolation != null) {
            final AccountEntity account = this.serviceLDAP.getAccountByAuthentication();
            final List<String> userRoles = account.getRoles().stream()
                    .map(RoleEntity::getType).collect(Collectors.toList());

            final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains(ADMIN);

            return this.violationService.getByIdJournal(journalViolation.getId()).getData().stream().map(violation -> {
                ViolationAccess violationAccess = new ViolationAccess();
                violationAccess.setId(violation.getId());
                violationAccess.setEdit(isAdmin || account.getEmployeeEntity().getPersonnelNumber()
                        .equals(violation.getWriter().getPersonnelNumber()));
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
    public JournalViolationEntity addJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody JournalViolationEntity violation) {
        if(violation.getName()== null)
            throw new BadRequestException("Имя не может быть пустым!");

        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();

        if(accountByAuthentication.getRoles().stream().map(RoleEntity::getType).filter(role -> !ADMIN_VIOLATION.equals(role)
                || !ADMIN.equals(role) || !COURATOR_VIOLATION.equals(role)).findFirst().isPresent()) {
            if(this.journalViolationService.getCountByPnumberWriter(accountByAuthentication
                    .getEmployeeEntity().getPersonnelNumber()) > 0) {
                throw new AccessDeniedException("Вы не можете больше создавать журналы!");
            }
        }

        JournalViolationEntity journalViolation = new JournalViolationEntity(violation);
        journalViolation.setWriter(accountByAuthentication.getEmployeeEntity());
        if(journalViolation.getDepartment() == null) {
            journalViolation.setDepartment(accountByAuthentication.getEmployeeEntity().getDepartment());
        } else {
            journalViolation.setDepartment(departmentService.getById(journalViolation.getDepartment().getId()));
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
    public JournalViolationEntity updateJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody JournalViolationEntity violation) {
        JournalViolationEntity journalViolation = this.journalViolationService.getById(violation.getId());
        if(violation.getDepartment() != null) {
            journalViolation.setDepartment(departmentService.getById(violation.getDepartment().getId()));
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
        this.violationService.delete(violations);
    }

    @ApiOperation(
            value = "Удалить журнал для нарушений",
            notes = "Удалить журнал для нарушений"
    )
    @UrlDeclaration(description = "Удалить журнал для нарушений", isAccessUser = true)
    @RequestMapping(value = "/journal/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteJournalViolation(@ApiParam(value = "Журнал нарушений") @RequestBody List<JournalViolationEntity> journalViolations) {
        journalViolations.stream().filter(journalViolation -> journalViolation.getId() != null)
                .forEach(journalViolation -> {
                    JournalViolationEntity journal = this.journalViolationService.getById(journalViolation.getId());
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
    public Page<JournalViolationEntity> getAllJournal(
            @ApiParam("Номер страници") @RequestParam(value = "page", defaultValue = "0") Integer number,
            @ApiParam("Размер страници") @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        final AccountEntity account = this.serviceLDAP.getAccountByAuthentication();
        final List<String> userRoles = account.getRoles().stream()
                .map(RoleEntity::getType).collect(Collectors.toList());

        final boolean isAdmin = userRoles.contains(ADMIN_VIOLATION) || userRoles.contains(ADMIN);

        if(isAdmin) {
            return this.journalViolationService.getAllJournal(number, pageSize);
        } else {
            final boolean isCourator = userRoles.contains(COURATOR_VIOLATION);

            return isCourator
                    ? this.journalViolationService.getAllByDep(account.getEmployeeEntity()
                    .getDepartment().getId(), number, pageSize)
                    : this.journalViolationService.getByPnumberWriter(account.getEmployeeEntity()
                    .getPersonnelNumber(), number, pageSize);
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
            updateViolation.setExecutor(this.employeeService.getByPersonnelNumber(violation.getExecutor().getPersonnelNumber()));
        }

        if(violation.isEffective() != null) {
            updateViolation.setEffective(violation.isEffective());
            if(violation.isEffective()) {
                updateViolation.setDateEndEliminationViolation(new Date());
            } else {
                updateViolation.setDateEndEliminationViolation(null);
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
                updateViolation.setExecutor(this.employeeService.getByPersonnelNumber(violation.getExecutor().getPersonnelNumber()));
            }

            if(violation.isEffective() != null) {
                updateViolation.setEffective(violation.isEffective());
                if(violation.isEffective()) {
                    updateViolation.setDateEndEliminationViolation(new Date());
                } else {
                    updateViolation.setDateEndEliminationViolation(null);
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
    public Violation addViolation(@ApiParam(value = "Журнал с нарушением") @RequestBody JournalViolationEntity journalViolation) {
        EmployeeEntity employeeEntity = serviceLDAP.getAccountByAuthentication().getEmployeeEntity();

        JournalViolationEntity generalJournalViolation = this.journalViolationService.getById(journalViolation.getId());
        if(generalJournalViolation == null)
            throw new BadRequestException("Не найден журнал с ID: " + journalViolation.getId());

        Violation lastAdd = null;
        if(journalViolation.getViolations().size() == 1) {
            final Violation v = journalViolation.getViolations().get(0);
            if (v.getTypeViolation() != null)
                v.setTypeViolation(this.typeViolationService.getById(v.getTypeViolation().getId()));
            v.setWriter(employeeEntity);
            v.setStartMakingViolation(new Date());
            v.setEffective(false);
            if (v.getExecutor() != null)
                v.setExecutor(this.employeeService.getByPersonnelNumber(v.getExecutor().getPersonnelNumber()));
            v.setJournalViolation(generalJournalViolation);
            lastAdd = new ViolationEntity(v);

            this.violationService.add(lastAdd);
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
    public List<TypeViolationEntity> getAllTypes() {
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
        final AccountEntity accountByAuthentication = this.serviceLDAP.getAccountByAuthentication();
        final EmployeeEntity employeeEntity = accountByAuthentication.getEmployeeEntity();
        final List<String> roleStream = this.serviceLDAP.getAccountByAuthentication().getRoles().stream()
                .map(RoleEntity::getType).collect(Collectors.toList());

        final boolean isAdmin = roleStream.contains(ADMIN_VIOLATION)
                        || roleStream.contains(ADMIN);

        if(isAdmin) {
            return this.violationService.getByIdJournal(id, number, pageSize);
        } else {
            final JournalViolationEntity journal = this.journalViolationService.getById(id);
            if(journal.getWriter().getPersonnelNumber().equals(employeeEntity.getPersonnelNumber())
                    || (journal.getDepartment().getId().equals(employeeEntity.getDepartment().getId())
                    && roleStream.contains(COURATOR_VIOLATION))) {
                return this.violationService.getByIdJournal(id, number, pageSize);
            }

            throw new AccessDeniedException("У вас нет доступа!");
        }
    }
}
