package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.beans.TypeServer;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

/**
 * Created by danilovey on 03.08.2016.
 */
@Repository
@Slf4j
public class DataBaseInitialization {

    @Autowired
    private SessionFactory sessionFactory;

    //@Autowired
    //private BankAccountDao bankAccountDao;

    @Autowired
    private TypeServer typeServer;

    /*@PostConstruct
    private void initOtherData() {
        if(typeServer.isTest())
            return;

        XSSFWorkbook myExcelBook = null;
        try {
            Resource resource = new ClassPathResource("bank_account.xlsx");
            myExcelBook = new XSSFWorkbook(resource.getInputStream());
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
            myExcelSheet.forEach(row -> {
                String initials = row.getCell(0).getStringCellValue();
                String cache = row.getCell(1).getStringCellValue();

                if(initials!= null){
                    initials = initials.trim();
                    cache = cache.trim();
                    if(!initials.isEmpty() && !cache.isEmpty()) {
                        final EmployeeEntity entity = new EmployeeDto();
                        entity.setInitials(initials);
                        final BankAccount account = new BankAccount(null, entity , cache);
                        this.bankAccountDao.persist(account);
                    }
                }
            });
            log.info("BackAccount size: {}", this.bankAccountDao.getCountAllAccount());
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
    }*/

    public void initDB() {
        //==============PSR=====================
        Session currentSession = this.sessionFactory.openSession();
        Transaction transaction = currentSession.getTransaction();
        try {
            transaction.begin();

            PsrStatus psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Новый");
            currentSession.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Открыт");
            currentSession.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Закрыт");
            currentSession.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Отклонен");
            currentSession.persist(psrStatus);

            psrStatus = new PsrStatusDecorator();
            psrStatus.setType("Утвержден");
            currentSession.persist(psrStatus);

            currentSession.flush();
            currentSession.clear();

            TypeViolationDecorator defaultTypeViolation = new TypeViolationDecorator();
            defaultTypeViolation.setName("Состояние ОТ соответствует требованиям");
            currentSession.persist(defaultTypeViolation);

            transaction.commit();
        } catch (Exception ex) {
            log.error("Невозжномно инициализировать БД!", ex);
            transaction.rollback();
        }
        currentSession.close();
    }
}
