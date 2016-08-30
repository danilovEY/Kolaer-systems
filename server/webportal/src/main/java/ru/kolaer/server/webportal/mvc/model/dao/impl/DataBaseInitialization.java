package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PsrStatusDao;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by danilovey on 03.08.2016.
 */
@Repository
public class DataBaseInitialization {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PsrStatusDao psrStatusDao;

    @Autowired
    private SessionFactory sessionFactory;

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

    @Transactional
    public void upgradeDataBase() {
        final RestTemplate restTemplate = new RestTemplate();
        final DbDataAll[] dbDataAlls = restTemplate.getForObject("http://js:8080/ru.kolaer.server.restful/database/dataAll/get/users/max", DbDataAll[].class);

        final GeneralRolesEntity userRole = new GeneralRolesEntityDecorator();
        userRole.setId(3);
        userRole.setType(EnumRole.USER);

        int i = 0;

        for(DbDataAll dbDataAll : dbDataAlls) {
            final GeneralEmployeesEntity dataBaseEmployee = new GeneralEmployeesEntityDecorator();
            dataBaseEmployee.setPnumber(dbDataAll.getPersonNumber());
            dataBaseEmployee.setPost(dbDataAll.getPost());
            dataBaseEmployee.setDepartament(dbDataAll.getDepartament());
            switch (dbDataAll.getGender()) {
                case "Мужской": {
                    dataBaseEmployee.setGender(EnumGender.MALE);
                    break;
                }
                case "Женский": {
                    dataBaseEmployee.setGender(EnumGender.FEMALE);
                    break;
                }
                default: {
                    dataBaseEmployee.setGender(EnumGender.MALE);
                    break;
                }
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

            sessionFactory.getCurrentSession().persist(dataBaseEmployee);
            sessionFactory.getCurrentSession().persist(dataBaseAccount);
            if(i != 16) i++; else {
                i = 0;
                sessionFactory.getCurrentSession().flush();
            }

            //this.employeeDao.persist(dataBaseEmployee);

            //this.accountDao.persist(dataBaseAccount);
        }
    }

}
