package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrAttachment;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.server.webportal.mvc.model.dao.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrAttachmentDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStateDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by danilovey on 03.08.2016.
 */
@Component
public class DataBaseInitialization {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private PsrStatusDao psrStatusDao;

    @Autowired
    private PsrRegisterDao psrRegisterDao;

    @Value("${secret_key}")
    private String secretKey;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibGen;


    @PostConstruct
    public void initDB() {
        if(hibGen.equals("create")) {
            //==============GENERAL=====================
            final GeneralAccountsEntity anonymousAccount = new GeneralAccountsEntityDecorator();
            anonymousAccount.setUsername("anonymous");
            anonymousAccount.setPassword(new StandardPasswordEncoder(secretKey).encode("anonymous"));

            final GeneralRolesEntity superAdminRole = new GeneralRolesEntityDecorator();
            superAdminRole.setType(EnumRole.SUPER_ADMIN);

            final GeneralRolesEntity adminRole = new GeneralRolesEntityDecorator();
            adminRole.setType(EnumRole.PSR_ADMIN);

            final GeneralRolesEntity userRole = new GeneralRolesEntityDecorator();
            userRole.setType(EnumRole.USER);

            final GeneralRolesEntity anoRole = new GeneralRolesEntityDecorator();
            anoRole.setType(EnumRole.ANONYMOUS);

            anonymousAccount.setRoles(Arrays.asList(anoRole));


            /*final RestTemplate restTemplate = new RestTemplate();
            //TODO: доделать!
            final DbDataAll[] dbDataAlls = restTemplate.getForObject("http://js:8080/ru.kolaer.server.restful/database/dataAll/get/users/max", DbDataAll[].class);

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
                dataBaseAccount.setUsername(dbDataAll.getLogin());
                dataBaseAccount.setPassword(new StandardPasswordEncoder(secretKey).encode(dbDataAll.getPassword()));
                dataBaseAccount.setEmail(dbDataAll.getEmail());
                dataBaseAccount.setRoles(Arrays.asList(userRole));
                dataBaseAccount.setGeneralEmployeesEntity(dataBaseEmployee);

                this.employeeDao.persist(dataBaseEmployee);

                this.accountDao.persist(dataBaseAccount);
            }*/

            //==============PSR=====================
            PsrStatus psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Новый");
            this.psrStatusDao.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Открыт");
            this.psrStatusDao.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Закрыт");
            this.psrStatusDao.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Отклонен");
            this.psrStatusDao.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Утвержден");
            this.psrStatusDao.persist(psrStatus);

            //============В БАЗУ====================
            this.roleDao.persist(superAdminRole);
            this.roleDao.persist(adminRole);
            this.roleDao.persist(userRole);
            this.roleDao.persist(anoRole);

            this.accountDao.persist(anonymousAccount);


        }
    }

}
