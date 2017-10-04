package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dto.PsrAccess;
import ru.kolaer.server.webportal.mvc.model.dto.PsrRegisterAccess;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterEntity;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStateEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrRegisterService;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrStatusService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 29.07.2016.
 */
@RestController
@RequestMapping(value = "/psr")
@Api(description = "Работа с ПСР-проектами", tags = "ПСР-проект")
public class PsrRegisterController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(PsrRegisterController.class);

    @Autowired
    private PsrRegisterService psrRegisterService;

    @Autowired
    private PsrStatusService psrStatusService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @Autowired
    private UrlSecurityService pathService;

    @ApiOperation(
            value = "Получить доступы"
    )
    @UrlDeclaration(description = "Получить доступы", isAccessAll = true)
    @RequestMapping(value = "/access", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrAccess getAllPsrAccess() {
        final PsrAccess psrAccess = new PsrAccess();

        final AccountEntity entity = this.serviceLDAP.getAccountByAuthentication();

        final List<String> roles = entity.getRoles().stream()
                .map(RoleEntity::getType).collect(Collectors.toList());

        List<String> accesses = this.pathService.getPathByUrl("/psr/get/all").getAccesses();

        boolean isGetAllProject = accesses.contains("ALL");
        if(!isGetAllProject)
            isGetAllProject = accesses.stream().filter(roles::contains).count() > 0;

        if(isGetAllProject) {
            final List<PsrRegisterAccess> psrRegisterAccess = new ArrayList<>();

            accesses = this.pathService.getPathByUrl("/psr/update/status").getAccesses();
            boolean isEditStatusAllProject = !accesses.contains("ALL")
                    && accesses.stream().filter(roles::contains).count() > 0;

            final List<PsrRegister> registers = this.psrRegisterService.getAll();
            registers.stream().forEach(psrRegister -> {
                final PsrRegisterAccess access = new PsrRegisterAccess();
                access.setId(psrRegister.getId());
                if (entity.getEmployeeEntity().getPersonnelNumber().equals(psrRegister.getAuthor().getPersonnelNumber())) {
                    if(psrRegister.getStatus().getType().equals("Новый")) {
                        access.setDeleteProject(true);
                    }
                    access.setEditNameComment(true);
                } else {
                    if(roles.contains("OIT") || roles.contains("ПСР Администратор")) {
                        access.setDeleteProject(true);
                        access.setEditNameComment(true);
                    }
                }

                access.setEditStatus(isEditStatusAllProject);

                psrRegisterAccess.add(access);
            });

            psrAccess.setPsrRegisterAccesses(psrRegisterAccess);
            psrAccess.setGettingAllPsrRegister(true);
        }

        return psrAccess;
    }

    @ApiOperation(
            value = "Добавить состояние ПСР-проектов"
    )
    @UrlDeclaration(description = "Добавить состояние проекта", isAccessUser = true)
    @RequestMapping(value = "/add/states", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister addPsrState(@ApiParam(value = "ПСР-проект") @RequestBody PsrRegister psrRegister) {
        final PsrRegister updateRegister = this.psrRegisterService.getById(psrRegister.getId());
        final List<PsrState> stateDecorators = new ArrayList<>();
        psrRegister.getStateList().forEach(psrState -> {
            if (psrState.getDate() != null && psrState.getComment() != null)
                stateDecorators.add(new PsrStateEntity(psrState));
        });
        List<PsrState> states = Optional.ofNullable(updateRegister.getStateList()).orElse(new ArrayList<>());
        states.addAll(stateDecorators);
        updateRegister.setStateList(states);
        this.psrRegisterService.update(updateRegister);
        return updateRegister;
    }

    @ApiOperation(
            value = "Обновить состояние ПСР-проектов"
    )
    @UrlDeclaration(description = "Обновить состояние проекта", isAccessUser = true)
    @RequestMapping(value = "/update/states", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister updatePsrState(@ApiParam(value = "ПСР-проект") @RequestBody PsrRegister psrRegister) {
        final PsrRegister updateRegister = this.psrRegisterService.getById(psrRegister.getId());
        updateRegister.getStateList().stream().forEach(psrState ->
            psrRegister.getStateList().forEach(psrStateForUpdate -> {
                if(psrState.getId().equals(psrStateForUpdate.getId())) {
                    psrState.setComment(psrStateForUpdate.getComment());
                    psrState.setDate(psrStateForUpdate.getDate());
                    psrState.setPlan(psrStateForUpdate.isPlan());
                }
            })
        );
        this.psrRegisterService.update(updateRegister);
        return updateRegister;
    }

    @ApiOperation(
            value = "Получить все статусы для ПСР-проектов"
    )
    @UrlDeclaration(description = "Получить все статусы", isAccessAll = true)
    @RequestMapping(value = "/status/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrStatus> getAllPsrStatus() {
        List<PsrStatus> list = this.psrStatusService.getAll();
        return list;
    }

    @ApiOperation(
            value = "Обновить статус у ПСР-проекта"
    )
    @UrlDeclaration(description = "Обновить статус у проекта", isAccessUser = true)
    @RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister updatePsrRegisterStatus(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister psrRegister) {
        if(psrRegister.getId() == null || psrRegister.getId() < 0)
            throw new BadRequestException("ID null или меньше нуля!");
        if(psrRegister.getStatus() != null && psrRegister.getStatus().getType() == null)
            throw new BadRequestException("Статус или тип ПСР-проекта = null!");

        final PsrRegister updatePsrRegister = this.psrRegisterService.getById(psrRegister.getId());
        updatePsrRegister.setStatus(this.psrStatusService.getStatusByType(psrRegister.getStatus().getType()));

        switch (updatePsrRegister.getStatus().getType()){
            case "Открыт": {
                updatePsrRegister.setDateOpen(new Date());
                updatePsrRegister.setDateClose(null);
                break;
            }
            case "Закрыт": { updatePsrRegister.setDateClose(new Date()); break;}
            default: break;
        }

        this.psrRegisterService.update(updatePsrRegister);
        return updatePsrRegister;
    }

    @ApiOperation(
            value = "Получить все ПСР-проекты"
    )
    @UrlDeclaration(description = "Получить все ПСР-проекты", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PsrRegister> getAllRegister(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                            @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return this.psrRegisterService.getAll(number, pageSize);
    }

    @ApiOperation(
            value = "Добавить ПСР-проект"
    )
    @UrlDeclaration(description = "Добавить ПСР-проект", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister addPsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterEntity(register);

        if(this.psrRegisterService.uniquePsrRegister(register)) {
            registerDto.setStatus(this.psrStatusService.getStatusByType("Новый"));
            registerDto.setAuthor(serviceLDAP.getAccountByAuthentication().getEmployeeEntity());

            this.psrRegisterService.add(registerDto);
            return registerDto;
        } else {
            throw new BadRequestException("ПСР-проект не уникальный!");
        }
    }

    @ApiOperation(
            value = "Удалить ПСР-проект"
    )
    @UrlDeclaration(description = "Удалить ПСР-проект", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removePsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        this.psrRegisterService.deletePstRegister(register.getId());
    }

    @ApiOperation(
            value = "Удалить список ПСР-проектов"
    )
    @UrlDeclaration(description = "Удалить ПСР-проекты", isAccessUser = true)
    @RequestMapping(value = "/delete/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removePsrRegisterList(
            @ApiParam(value = "ПСР-проекты", required = true) @RequestBody List<PsrRegister> registers) {
        this.psrRegisterService.deletePstRegisterListById(registers);
    }

    @ApiOperation(
            value = "Обновить ПСР-проект"
    )
    @UrlDeclaration(description = "Обновить ПСР-проект", isAccessUser = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister updatePsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        final PsrRegister updatePsrRegister = this.psrRegisterService.getById(register.getId());
        if(updatePsrRegister != null) {
            updatePsrRegister.setName(register.getName());
            updatePsrRegister.setComment(register.getComment());
            this.psrRegisterService.update(updatePsrRegister);
            return updatePsrRegister;
        } else {
            throw new BadRequestException("ПСР-проект по id: " + register.getId() + " не найден!");
        }
    }

    @ApiOperation(
            value = "Получить все ПСР-проекты",
            notes = "Только id и имя"
    )
    @UrlDeclaration(description = "Получить все ПСР-проект. (только id и имя)", isAccessAll = true)
    @RequestMapping(value = "/get/all/id-name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegisterWithIdAndName() {
        List<PsrRegister> list = this.psrRegisterService.getIdAndNamePsrRegisters();
        return list;
    }
}
