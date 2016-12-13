package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.RegisterTicketScheduler;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by danilovey on 18.11.2016.
 */
@RestController
@RequestMapping(value = "/examples")
@Api( tags = "Примеры", description = "Примеры объектов", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ExampleController {

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(
            value = "Получить пример журнала нарушений",
            notes = "Получить пример журнала нарушений"
    )
    @UrlDeclaration(description = "Пример нарушений", isAccessUser = true)
    @RequestMapping(value = "/violations/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JournalViolation> insertViolations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new IllegalArgumentException("Authentication is null");

        final GeneralEmployeesEntity generalEmployeesEntity = this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()).getGeneralEmployeesEntity();

        final TypeViolationDecorator typeViolationDecorator = new TypeViolationDecorator();
        typeViolationDecorator.setId(0);
        typeViolationDecorator.setName("Тип нарушения1");

        final ViolationDecorator violationDecorator = new ViolationDecorator();
        violationDecorator.setTypeViolation(typeViolationDecorator);
        violationDecorator.setStageEnum(StageEnum.I);
        violationDecorator.setEffective(false);
        violationDecorator.setViolation("<p>Нарушение1</p>");
        violationDecorator.setExecutor(generalEmployeesEntity);
        violationDecorator.setWriter(generalEmployeesEntity);
        violationDecorator.setDateEndEliminationViolation(new Date());
        violationDecorator.setDateLimitEliminationViolation(new Date());
        violationDecorator.setStartMakingViolation(new Date());
        violationDecorator.setTodo("<p>Todo1</p>");

        final JournalViolationDecorator journalViolationDecorator = new JournalViolationDecorator();
        journalViolationDecorator.setName("Новый журнал нарушений1");
        journalViolationDecorator.setViolations(Arrays.asList(violationDecorator));

        return ResponseEntity.ok(journalViolationDecorator);
    }

}
