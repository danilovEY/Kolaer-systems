package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.bank.BankAccount;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import javax.annotation.PostConstruct;
import javax.persistence.ManyToMany;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 03.08.2016.
 */
@Repository
@Slf4j
public class DataBaseInitialization {
    private static final Logger LOG = LoggerFactory.getLogger(DataBaseInitialization.class);

    @Autowired
    @Qualifier(value = "dataSourceKolaerBase")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BankAccountDao bankAccountDao;

    @PostConstruct
    private void initOtherData() {
        XSSFWorkbook myExcelBook = null;
        try {
            myExcelBook = new XSSFWorkbook(this.getClass().getResourceAsStream("bank_account.xlsx"));
            XSSFSheet myExcelSheet = myExcelBook.getSheet("KOLAER");
            myExcelSheet.forEach(row -> {
                String initials = row.getCell(0).getStringCellValue();
                String cache = row.getCell(1).getStringCellValue();

                if(initials!= null && cache!= null){
                    initials = initials.trim();
                    cache = cache.trim();
                    if(!initials.isEmpty() && !cache.isEmpty()) {
                        final GeneralEmployeesEntity entity = new GeneralEmployeesEntityBase();
                        entity.setInitials(initials);
                        final BankAccount account = new BankAccount(entity , cache);
                        this.bankAccountDao.persist(account);
                    }
                }
            });
        } catch (IOException e) {
            log.error("Ошибка при чтении файла!", e);
        } finally {
            if(myExcelBook != null) {
                try {
                    myExcelBook.close();
                } catch (IOException e) {
                    log.error("Ошибка закрытии файла!", e);
                }
            }
        }
    }

    @Transactional
    public void initDB() {
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

        JournalViolation journalViolation = new JournalViolationDecorator();
        journalViolation.setName("Общий журнал");
        this.sessionFactory.getCurrentSession().persist(journalViolation);
    }

    @Transactional
    public void updateDataBase() {
        final List<DbDataAll> dbDataAlls = this.jdbcTemplate.query("SELECT * FROM db_data_all", ((rs, rowNum) -> {
            final DbDataAll dbDataAll = new DbDataAll();
            dbDataAll.setPersonNumber(rs.getInt("person_number"));
            dbDataAll.setInitials(rs.getString("initials"));
            dbDataAll.setDepartament(rs.getString("departament"));
            dbDataAll.setDepartamentAbbreviated(rs.getString("departament_abbreviated"));
            dbDataAll.setPost(rs.getString("post"));
            dbDataAll.setGender(rs.getString("gender"));
            dbDataAll.setEmail(rs.getString("email"));
            dbDataAll.setPhone(rs.getString("phone"));
            dbDataAll.setMobilePhone(rs.getString("mobile_phone"));
            dbDataAll.setVCard(rs.getString("vCard"));
            dbDataAll.setBirthday(rs.getDate("birthday"));
            return dbDataAll;
        }));
        int i = 0;

        final List<GeneralEmployeesEntity> generalEmployeesEntities = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator").list();
        Map<Integer, GeneralEmployeesEntity> mapEmployee = new HashMap<>();
        generalEmployeesEntities.forEach(generalEmployeesEntity -> mapEmployee.put(generalEmployeesEntity.getPnumber(), generalEmployeesEntity));

        List<GeneralDepartamentEntity> departamentEntities = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralDepartamentEntityDecorator").list();
        Map<String, GeneralDepartamentEntity> mapDep = departamentEntities.stream().collect(Collectors.toMap(GeneralDepartamentEntity::getName, d -> d));
        for (DbDataAll dbDataAll : dbDataAlls) {
            GeneralEmployeesEntity dataBaseEmployee = mapEmployee.get(dbDataAll.getPersonNumber());

            if(dataBaseEmployee == null) {
                dataBaseEmployee = new GeneralEmployeesEntityDecorator();
                dataBaseEmployee.setPnumber(dbDataAll.getPersonNumber());
                dataBaseEmployee.setPost(dbDataAll.getPost());
                dataBaseEmployee.setInitials(dbDataAll.getInitials());
                dataBaseEmployee.setPhoneNumber(dbDataAll.getPhone());
                dataBaseEmployee.setMobileNumber(dbDataAll.getMobilePhone());
                dataBaseEmployee.setBirthday(dbDataAll.getBirthday());
                dataBaseEmployee.setPhoto(dbDataAll.getVCard());
                try {
                    dataBaseEmployee.setPhoto("http://asupkolaer/app_ie8/assets/images/vCard/o_" + URLEncoder.encode(dbDataAll.getInitials(), "UTF-8").replace("+", "%20") + ".jpg");
                } catch (UnsupportedEncodingException e) {
                    LOG.error("Невозможно преобразовать {} в URL!", dbDataAll.getInitials(), e);
                    dataBaseEmployee.setPhoto(dbDataAll.getVCard());
                }
                dataBaseEmployee.setEmail(dbDataAll.getEmail());
                dataBaseEmployee.setGender(dbDataAll.getGender());

                if (dbDataAll.getDepartament() != null && !dbDataAll.getDepartament().trim().isEmpty()) {
                    this.updateDepartament(dataBaseEmployee, dbDataAll, mapDep.get(dbDataAll.getDepartament()));
                }
            } else {
                dataBaseEmployee.setPost(dbDataAll.getPost());
                dataBaseEmployee.setInitials(dbDataAll.getInitials());
                dataBaseEmployee.setPhoneNumber(dbDataAll.getPhone());
                dataBaseEmployee.setMobileNumber(dbDataAll.getMobilePhone());
                dataBaseEmployee.setBirthday(dbDataAll.getBirthday());
                try {
                    dataBaseEmployee.setPhoto("http://asupkolaer/app_ie8/assets/images/vCard/o_" + URLEncoder.encode(dbDataAll.getInitials(), "UTF-8").replace("+", "%20") + ".jpg");
                } catch (UnsupportedEncodingException e) {
                    LOG.error("Невозможно преобразовать {} в URL!", dbDataAll.getInitials(), e);
                    dataBaseEmployee.setPhoto(dbDataAll.getVCard());
                }
                dataBaseEmployee.setEmail(dbDataAll.getEmail());

                this.updateDepartament(dataBaseEmployee, dbDataAll, mapDep.get(dbDataAll.getDepartament()));
                mapEmployee.remove(dataBaseEmployee.getPnumber());
            }

            if (i == 50) {
                i = 0;
                this.sessionFactory.getCurrentSession().flush();
            } else {
                i++;
            }
        }

        mapEmployee.values().forEach(generalEmployeesEntity -> this.sessionFactory.getCurrentSession().delete(generalEmployeesEntity));
    }

    private void updateDepartament(GeneralEmployeesEntity entity, DbDataAll dbDataAll, GeneralDepartamentEntity dep) {
        GeneralDepartamentEntity generalDepartamentEntity = dep;

        if (generalDepartamentEntity == null) {
            generalDepartamentEntity = new GeneralDepartamentEntityDecorator();
            generalDepartamentEntity.setName(dbDataAll.getDepartament());
            final String abbrev = dbDataAll.getDepartament().substring(0, dbDataAll.getDepartament().indexOf(" ")).trim();
            generalDepartamentEntity.setAbbreviatedName(abbrev);

            if (dbDataAll.getPost().contains("Начальник") || dbDataAll.getPost().equals("Директор")
                    || dbDataAll.getPost().equals("Руководитель") || dbDataAll.getPost().equals("Ведущий")
                    || dbDataAll.getPost().equals("Главный")) {
                generalDepartamentEntity.setChiefEntity(entity.getPnumber());
            }

            this.sessionFactory.getCurrentSession().persist(generalDepartamentEntity);
            entity.setDepartament(generalDepartamentEntity);
            this.sessionFactory.getCurrentSession().persist(entity);
        } else {
            entity.setDepartament(generalDepartamentEntity);
            this.sessionFactory.getCurrentSession().persist(entity);

            if (dbDataAll.getPost().contains("Начальник") || dbDataAll.getPost().equals("Директор")
                    || dbDataAll.getPost().equals("Руководитель") || dbDataAll.getPost().equals("Ведущий")
                    || dbDataAll.getPost().equals("Главный")) {
                generalDepartamentEntity.setChiefEntity(dbDataAll.getPersonNumber());
                this.sessionFactory.getCurrentSession().update(generalDepartamentEntity);
            }
        }
    }

}
