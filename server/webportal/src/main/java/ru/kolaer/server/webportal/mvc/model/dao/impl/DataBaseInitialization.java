package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrAttachment;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
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
            final GeneralAccountsEntity superAdminAccount = new GeneralAccountsEntityDecorator();
            superAdminAccount.setUsername("kolaeradmin");
            superAdminAccount.setPassword(new StandardPasswordEncoder(secretKey).encode("kolaeradmin"));
            superAdminAccount.setEmail("oit@kolaer.ru");

            final GeneralAccountsEntity anonymousAccount = new GeneralAccountsEntityDecorator();
            anonymousAccount.setUsername("anonymous");
            anonymousAccount.setPassword(new StandardPasswordEncoder(secretKey).encode("anonymous"));

            final GeneralRolesEntity superAdminRole = new GeneralRolesEntityDecorator();
            superAdminRole.setType(EnumRole.SUPER_ADMIN);

            final GeneralRolesEntity adminRole = new GeneralRolesEntityDecorator();
            adminRole.setType(EnumRole.ADMIN);

            final GeneralRolesEntity userRole = new GeneralRolesEntityDecorator();
            userRole.setType(EnumRole.USER);

            final GeneralRolesEntity anoRole = new GeneralRolesEntityDecorator();
            anoRole.setType(EnumRole.ANONYMOUS);

            superAdminAccount.setRoles(Arrays.asList(superAdminRole));
            anonymousAccount.setRoles(Arrays.asList(anoRole));

            final GeneralEmployeesEntity superAdminEmployee = new GeneralEmployeesEntityDecorator();
            superAdminEmployee.setPost("Администратор");
            superAdminEmployee.setDepartament("ОИТ");
            superAdminEmployee.setGender(EnumGender.MALE);
            superAdminEmployee.setInitials("Администратор");
            //superAdminEmployee.setAccountsEntity(Arrays.asList(superAdminAccount));

            superAdminAccount.setGeneralEmployeesEntity(superAdminEmployee);
            //==============PSR=====================
            final PsrStatus psrStatusNew = new PsrStatusDecorator();
            psrStatusNew.setType("Новый");

            final PsrAttachment psrAttachment = new PsrAttachmentDecorator();
            psrAttachment.setName("D:/test.txt");

            final PsrState psrState = new PsrStateDecorator();
            psrState.setPlan(true);
            psrState.setDate(new Date());
            psrState.setComment("Cccccccccs");

            final PsrRegister psrRegister = new PsrRegisterDecorator();
            psrRegister.setStatus(psrStatusNew);
            psrRegister.setAttachments(Arrays.asList(psrAttachment));
            psrRegister.setAuthor(superAdminEmployee);
            psrRegister.setComment("ASDASD");
            psrRegister.setDateOpen(new Date());
            psrRegister.setName("QQQQQQ");
            psrRegister.setStateList(Arrays.asList(psrState));


            //============В БАЗУ====================

            this.roleDao.persist(superAdminRole);
            this.roleDao.persist(adminRole);
            this.roleDao.persist(userRole);
            this.roleDao.persist(anoRole);

            this.employeeDao.persist(superAdminEmployee);

            this.accountDao.persist(superAdminAccount);
            this.accountDao.persist(anonymousAccount);

            this.psrStatusDao.persist(psrStatusNew);

            this.psrRegisterDao.persist(psrRegister);
        }
    }

}
