package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.EnumCategory;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.ExcelReader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
public class ExcelReaderEmployee implements ExcelReader<EmployeeEntity> {
    private static int YEAR_NOT_DISMISSAL = 9999;
    private static String FIO_NAME = "ФИО";
    private static String FIO_NAME_2 = "Табельный номер";
    private static String SECOND_NAME = "Фамилия";
    private static String FIRST_NAME = "Имя";
    private static String THIRD_NAME = "Отчество";
    private static String SEX = "Пол";
    private static String EMPLOYMENT_DATE = "Поступл.";
    private static String BIRTHDAY_DATE = "ДатаРожд";
    private static String PERSONNEL_NUMBER = "Таб.№";
    private static String CATEGORY = "Категория сотрудников";

    private static String WORKER_CATEGORY = "РабочийОкладЧас";
    private static String SPEC_CATEGORY = "СпециалистОкладЧас";
    private static String LEADER_CATEGORY = "РуководитОкладЧас";
    private static String EMPLOYEES_CATEGORY = "СлужащийОкладЧас";

    @Override
    public EmployeeEntity parse(XSSFRow row, List<String> nameColumns) {
        EmployeeEntity newEmployeeEntity = new EmployeeEntity();

        String value = getStringValue(nameColumns, PERSONNEL_NUMBER, row);
        newEmployeeEntity.setPersonnelNumber(value != null ? Long.valueOf(value) : null);

        value = getStringValue(nameColumns, FIRST_NAME, row);
        newEmployeeEntity.setFirstName(value);

        value = getStringValue(nameColumns, SECOND_NAME, row);
        newEmployeeEntity.setSecondName(value);

        value = getStringValue(nameColumns, THIRD_NAME, row);
        newEmployeeEntity.setThirdName(value);

        newEmployeeEntity.setInitials(newEmployeeEntity.getSecondName() + " "
                + newEmployeeEntity.getFirstName() + " "
                + newEmployeeEntity.getThirdName());

        if(StringUtils.isEmpty(newEmployeeEntity.getInitials())) {
            value = Optional.ofNullable(getStringValue(nameColumns, FIO_NAME_2, row))
                    .orElse(getStringValue(nameColumns, FIO_NAME, row));

            newEmployeeEntity.setInitials(value);

            if(StringUtils.hasText(value)) {
                String[] fio = value.split(" ");

                if(fio.length > 0) {
                    newEmployeeEntity.setSecondName(fio[0]);
                }

                if(fio.length > 1) {
                    newEmployeeEntity.setFirstName(fio[1]);
                }

                if(fio.length > 2) {
                    newEmployeeEntity.setThirdName(fio[3]);
                }
            }
        }

        Date date = getDateValue(nameColumns, BIRTHDAY_DATE, row);
        newEmployeeEntity.setBirthday(date);

        date = getDateValue(nameColumns, EMPLOYMENT_DATE, row);
        newEmployeeEntity.setEmploymentDate(date);

        value = getStringValue(nameColumns, SEX, row);
        if("мужской".equals(value.toLowerCase())) {
            newEmployeeEntity.setGender(EnumGender.MALE);
        } else {
            newEmployeeEntity.setGender(EnumGender.FEMALE);
        }

        value = getStringValue(nameColumns, CATEGORY, row);
        if(value != null) {
            newEmployeeEntity.setCategory(getCategory(value));
        }

        try {
            newEmployeeEntity.setPhoto("http://asupkolaer.local/app_ie8/assets/images/vCard/o_"
                    + URLEncoder.encode(newEmployeeEntity.getInitials(), "UTF-8")
                    .replace("+", "%20") + ".jpg");
        } catch (UnsupportedEncodingException ignore) {
            log.warn("User initials can't encode to UTF-8", newEmployeeEntity.getInitials());
        }

        log.debug("Parse employee: {}", newEmployeeEntity);

        return newEmployeeEntity;
    }

    private EnumCategory getCategory(String category) {
        if(WORKER_CATEGORY.equals(category)) return EnumCategory.WORKER;
        if(SPEC_CATEGORY.equals(category)) return EnumCategory.SPECIALIST;
        if(LEADER_CATEGORY.equals(category)) return EnumCategory.LEADER;
        if(EMPLOYEES_CATEGORY.equals(category)) return EnumCategory.EMPLOYEE;
        return null;
    }

    @Override
    public EmployeeEntity checkValues(XSSFRow row, EmployeeEntity object) {
        if(object == null) {
            throw new IllegalArgumentException("Прочитанное подразделение NULL! Строка: " + (row.getRowNum() + 1));
        }

        if(StringUtils.isEmpty(object.getInitials()) || StringUtils.isEmpty(object.getFirstName())
                || StringUtils.isEmpty(object.getSecondName()) || StringUtils.isEmpty(object.getThirdName())
                || object.getPersonnelNumber() == null || object.getGender() == null) {
            throw new UnexpectedRequestParams("У сотрудника пустое Ф.И.О или табельный или пол! Строка: " + (row.getRowNum() + 1), object);
        }

        return object;
    }
}
