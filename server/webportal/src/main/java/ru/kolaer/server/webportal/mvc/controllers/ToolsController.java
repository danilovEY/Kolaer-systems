package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@RequestMapping("/non-security/tools")
public class ToolsController {

    @Value("${secret_key}")
    private String secretKey;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value = "get/time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DateTimeJson getTime() {
        final DateTimeJson dateTimeJson = new DateTimeJson();
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        dateTimeJson.setData(localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        dateTimeJson.setTime(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return dateTimeJson;
    }

    @RequestMapping(value = "update", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateDb() {
        //TODO: DON'T LOOK!!!!!
        final RestTemplate restTemplate = new RestTemplate();
        final DbDataAll[] dbDataAlls = restTemplate.getForObject("http://js:8080/ru.kolaer.server.restful/database/dataAll/get/users/max", DbDataAll[].class);

        final GeneralRolesEntity userRole = new GeneralRolesEntityDecorator();
        userRole.setId(3);
        userRole.setType(EnumRole.USER);

        for(DbDataAll dbDataAll : dbDataAlls) {
            final GeneralEmployeesEntity dataBaseEmployee = new GeneralEmployeesEntityDecorator();
            dataBaseEmployee.setPnumber(dbDataAll.getPersonNumber());
            dataBaseEmployee.setPost(dbDataAll.getPost());
            dataBaseEmployee.setDepartament(dbDataAll.getDepartament());
            switch (dbDataAll.getGender()) {
                case "Мужской": {dataBaseEmployee.setGender(EnumGender.MALE); break;}
                case "Женский": {dataBaseEmployee.setGender(EnumGender.FEMALE); break;}
                default: {dataBaseEmployee.setGender(EnumGender.MALE); break;}
            }
            dataBaseEmployee.setInitials(dbDataAll.getInitials());
            dataBaseEmployee.setPhoneNumber(dbDataAll.getPhone());
            dataBaseEmployee.setMobileNumber(dbDataAll.getMobilePhone());

            final GeneralAccountsEntity dataBaseAccount = new GeneralAccountsEntityDecorator();
            dataBaseAccount.setUsername(Optional.ofNullable(dbDataAll.getLogin()).orElse(dataBaseEmployee.getPnumber().toString()));
            dataBaseAccount.setPassword(new StandardPasswordEncoder(secretKey).encode(Optional.ofNullable(dbDataAll.getPassword()).orElse(dataBaseAccount.getUsername())));
            dataBaseAccount.setEmail(dbDataAll.getEmail());
            dataBaseAccount.setRoles(Arrays.asList(userRole));
            dataBaseAccount.setGeneralEmployeesEntity(dataBaseEmployee);

            this.employeeDao.persist(dataBaseEmployee);

            this.accountDao.persist(dataBaseAccount);
        }
    }
}
