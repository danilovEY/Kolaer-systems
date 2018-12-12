package ru.kolaer.server.webportal.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;

/**
 * Created by danilovey on 18.11.2016.
 */
//@RestController
//@RequestMapping(value = "/examples")
@Api( tags = "Примеры", description = "Примеры объектов", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ExampleController {
/*
    @Autowired
    private AuthenticationService serviceLDAP;

    @ApiOperation(
            value = "Получить пример журнала нарушений"
    )
    @UrlDeclaration(description = "Пример нарушений", isAccessUser = true)
    @RequestMapping(value = "/violations/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JournalViolationEntity> insertViolations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final EmployeeEntity employeeEntity = this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()).getEmployeeEntity();

        final ViolationTypeEntity typeViolationDecorator = new ViolationTypeEntity();
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
*/
}
