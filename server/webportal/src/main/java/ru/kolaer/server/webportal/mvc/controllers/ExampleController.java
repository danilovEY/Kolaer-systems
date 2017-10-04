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
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by danilovey on 18.11.2016.
 */
@RestController
@RequestMapping(value = "/examples")
@Api( tags = "Примеры", description = "Примеры объектов", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ExampleController extends BaseController {

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(
            value = "Получить пример журнала нарушений"
    )
    @UrlDeclaration(description = "Пример нарушений", isAccessUser = true)
    @RequestMapping(value = "/violations/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JournalViolationEntity> insertViolations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final EmployeeEntity employeeEntity = this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()).getEmployeeEntity();

        final TypeViolationEntity typeViolationDecorator = new TypeViolationEntity();
        typeViolationDecorator.setId(0);
        typeViolationDecorator.setName("Тип нарушения1");

        final ViolationEntity violationDecorator = new ViolationEntity();
        violationDecorator.setTypeViolation(typeViolationDecorator);
        violationDecorator.setStageEnum(StageEnum.I);
        violationDecorator.setEffective(false);
        violationDecorator.setViolation("<p>Нарушение1</p>");
        violationDecorator.setExecutor(employeeEntity);
        violationDecorator.setWriter(employeeEntity);
        violationDecorator.setDateEndEliminationViolation(new Date());
        violationDecorator.setDateLimitEliminationViolation(new Date());
        violationDecorator.setStartMakingViolation(new Date());
        violationDecorator.setTodo("<p>Todo1</p>");

        final JournalViolationEntity journalViolationDecorator = new JournalViolationEntity();
        journalViolationDecorator.setName("Новый журнал нарушений1");
        journalViolationDecorator.setViolations(Arrays.asList(violationDecorator));

        return ResponseEntity.ok(journalViolationDecorator);
    }

}
