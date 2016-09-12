package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import java.util.List;

/**
 * Created by danilovey on 03.08.2016.
 */
@Repository
public class DataBaseInitialization {
    private static final Logger LOG = LoggerFactory.getLogger(DataBaseInitialization.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibGen;

    @Transactional
    public void initDB() {
        //==============GENERAL-ROLE=====================
        final GeneralRolesEntity superAdminRole = new GeneralRolesEntityDecorator();
        superAdminRole.setType(EnumRole.SUPER_ADMIN);

        final GeneralRolesEntity adminRole = new GeneralRolesEntityDecorator();
        adminRole.setType(EnumRole.PSR_ADMIN);

        final GeneralRolesEntity userRole = new GeneralRolesEntityDecorator();
        userRole.setType(EnumRole.USER);

        final GeneralRolesEntity anoRole = new GeneralRolesEntityDecorator();
        anoRole.setType(EnumRole.ANONYMOUS);

        //==============PSR=====================
        PsrStatus psrStatus = new PsrStatusDecorator();
        psrStatus.setType("Новый");
        this.sessionFactory.getCurrentSession().persist(psrStatus);

        psrStatus = new PsrStatusDecorator();
        psrStatus.setType("Открыт");
        this.sessionFactory.getCurrentSession().persist(psrStatus);

        psrStatus = new PsrStatusDecorator();
        psrStatus.setType("Закрыт");
        this.sessionFactory.getCurrentSession().persist(psrStatus);

        psrStatus = new PsrStatusDecorator();
        psrStatus.setType("Отклонен");
        this.sessionFactory.getCurrentSession().persist(psrStatus);

        psrStatus = new PsrStatusDecorator();
        psrStatus.setType("Утвержден");
        this.sessionFactory.getCurrentSession().persist(psrStatus);

        //============В БАЗУ====================
        this.sessionFactory.getCurrentSession().persist(superAdminRole);
        this.sessionFactory.getCurrentSession().persist(adminRole);
        this.sessionFactory.getCurrentSession().persist(userRole);
        this.sessionFactory.getCurrentSession().persist(anoRole);
    }

    @Transactional
    public void updateDataBase() {
        final RestTemplate restTemplate = new RestTemplate();

        final DbDataAll[] dbDataAlls = restTemplate.getForObject("http://js:8080/ru.kolaer.server.restful/database/dataAll/get/users/max", DbDataAll[].class);
        int i = 0;
        for (DbDataAll dbDataAll : dbDataAlls) {
            final GeneralEmployeesEntity dataBaseEmployee = new GeneralEmployeesEntityDecorator();
            dataBaseEmployee.setPnumber(dbDataAll.getPersonNumber());
            dataBaseEmployee.setPost(dbDataAll.getPost());
            dataBaseEmployee.setInitials(dbDataAll.getInitials());
            dataBaseEmployee.setPhoneNumber(dbDataAll.getPhone());
            dataBaseEmployee.setMobileNumber(dbDataAll.getMobilePhone());
            dataBaseEmployee.setBirthday(dbDataAll.getBirthday());

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
            if (dbDataAll.getDepartament() != null || !dbDataAll.getDepartament().trim().isEmpty()) {
                List<GeneralDepartamentEntity> name = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralDepartamentEntityDecorator dep WHERE dep.name = :name").setParameter("name", dbDataAll.getDepartament()).list();

                GeneralDepartamentEntity generalDepartamentEntity = null;

                if (name.size() > 0) {
                    generalDepartamentEntity = name.get(0);
                }

                if (generalDepartamentEntity == null) {
                    generalDepartamentEntity = new GeneralDepartamentEntityDecorator();
                    generalDepartamentEntity.setName(dbDataAll.getDepartament());
                    final String abbrev = dbDataAll.getDepartament().substring(0, dbDataAll.getDepartament().indexOf(" ")).trim();
                    generalDepartamentEntity.setAbbreviatedName(abbrev);

                    if (dbDataAll.getPost().contains("Начальник") || dbDataAll.getPost().equals("Директор")) {
                        generalDepartamentEntity.setChiefEntity(dataBaseEmployee);
                    }

                    this.sessionFactory.getCurrentSession().persist(generalDepartamentEntity);
                    dataBaseEmployee.setDepartament(generalDepartamentEntity);
                    this.sessionFactory.getCurrentSession().persist(dataBaseEmployee);
                } else {
                    dataBaseEmployee.setDepartament(generalDepartamentEntity);
                    this.sessionFactory.getCurrentSession().persist(dataBaseEmployee);

                    if (dbDataAll.getPost().contains("Начальник") || dbDataAll.getPost().equals("Директор")) {
                        generalDepartamentEntity.setChiefEntity(dataBaseEmployee);
                        this.sessionFactory.getCurrentSession().update(dataBaseEmployee);
                    }
                }
            }

            if (i == 50) {
                i = 0;
                this.sessionFactory.getCurrentSession().flush();
            } else {
                i++;
            }
        }
    }

}
