package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

//@RestController
//@RequestMapping(value = "/violations")
public class ViolationController {

    @Autowired
    private ViolationService violationService;

    @Autowired
    private TypeViolationService typeViolationService;

    @Autowired
    private JournalViolationService journalViolationService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @UrlDeclaration(description = "Тестовая ссылка")
    @RequestMapping(value = "/test/insert", method = RequestMethod.GET)
    public ResponseEntity<List<JournalViolation>> insertViolations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new IllegalArgumentException("Authentication is null");

        final GeneralEmployeesEntity generalEmployeesEntity = this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()).getGeneralEmployeesEntity();

        final TypeViolationDecorator typeViolationDecorator = new TypeViolationDecorator();
        typeViolationDecorator.setName("Тип нарушения1");

        final ViolationDecorator violationDecorator = new ViolationDecorator();
        violationDecorator.setTypeViolation(typeViolationDecorator);
        violationDecorator.setStageEnum(StageEnum.I);
        violationDecorator.setEffective(false);
        violationDecorator.setViolation("Нарушение1");
        violationDecorator.setExecutor(generalEmployeesEntity);
        violationDecorator.setWriter(generalEmployeesEntity);
        violationDecorator.setDateEndEliminationViolation(new Date());
        violationDecorator.setDateLimitEliminationViolation(new Date());
        violationDecorator.setStartMakingViolation(new Date());
        violationDecorator.setTodo("Todo1");

        final JournalViolationDecorator journalViolationDecorator = new JournalViolationDecorator();
        journalViolationDecorator.setName("Новый журнал нарушений1");
        journalViolationDecorator.setViolations(Arrays.asList(violationDecorator));

        this.journalViolationService.add(journalViolationDecorator);

        List<JournalViolation> all = this.journalViolationService.getAll();
        return ResponseEntity.ok(all);
    }


}
