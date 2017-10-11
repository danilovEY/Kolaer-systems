package ru.kolaer.server.webportal.mvc.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 03.10.2017.
 */
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
@Slf4j
public class BankAccountController {
    private final BankAccountDao bankAccountDao;
    private final EmployeeDao employeeDao;

    @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BankAccountEntity addAccount(@RequestBody BankAccountEntity bankAccount) {
        bankAccountDao.persist(bankAccount);
        return bankAccount;
    }


    @RequestMapping(value = "update", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BankAccountEntity> updateAccount() {
        List<BankAccountEntity> accounts = new ArrayList<>();

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
                        List<EmployeeEntity> employeeByInitials = employeeDao.findEmployeeByInitials(initials);
                        if(!employeeByInitials.isEmpty()) {
                            EmployeeEntity employeeEntity = employeeByInitials.stream().findFirst().get();
                            final BankAccountEntity account = new BankAccountEntity(null, null, employeeEntity, cache);
                            this.bankAccountDao.persist(account);
                            accounts.add(account);
                        }
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

        return accounts;
    }
}
