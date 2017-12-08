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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
public class ExcelReaderEmployee implements ExcelReader<EmployeeEntity> {
    private static int YEAR_NOT_DISMISSAL = 9999;
    private static String FIO_NAME = "ФИО";
    private static String SECOND_NAME = "Фамилия";
    private static String FIRST_NAME = "Имя";
    private static String THIRD_NAME = "Отчество";
    private static String SEX = "Пол";
    private static String EMPLOYMENT_DATE = "Поступл.";
    private static String DISMISSAL_DATE = "Дата увольнения";
    private static String BIRTHDAY_DATE = "ДатаРожд";
    private static String PERSONNEL_NUMBER = "Таб.№";
    private static String PHONE_NUMBER = "Телефон";
    private static String EMAIL = "Эл. почта(MAIL)";
    private static String CATEGORY = "Категория сотрудников";

    private static String WORKER_CATEGORY = "РабочийОкладЧас";
    private static String SPEC_CATEGORY = "СпециалистОкладЧас";
    private static String LEADER_CATEGORY = "РуководитОкладЧас";

    @Override
    public EmployeeEntity parse(XSSFRow row, List<String> nameColumns) {
        String value = getStringValue(nameColumns, PERSONNEL_NUMBER, row);

        Long pNumber = Long.valueOf(value);

        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        newEmployeeEntity.setPersonnelNumber(pNumber);

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
            value = getStringValue(nameColumns, FIO_NAME, row);

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

        date = getDateValue(nameColumns, DISMISSAL_DATE, row);
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (calendar.get(Calendar.YEAR) != YEAR_NOT_DISMISSAL) {
                newEmployeeEntity.setDismissalDate(date);
            }
        }

        value = getStringValue(nameColumns, SEX, row);
        if("мужской".equals(value.toLowerCase())) {
            newEmployeeEntity.setGender(EnumGender.MALE);
        } else {
            newEmployeeEntity.setGender(EnumGender.FEMALE);
        }

        value = getStringValue(nameColumns, PHONE_NUMBER, row);
        newEmployeeEntity.setHomePhoneNumber(value);

        value = getStringValue(nameColumns, CATEGORY, row);
        if(value != null) {
            newEmployeeEntity.setCategory(getCategory(value));
        }

        value = getStringValue(nameColumns, EMAIL, row);
        newEmployeeEntity.setEmail(value);

        try {
            newEmployeeEntity.setPhoto("http://asupkolaer.local/app_ie8/assets/images/vCard/o_"
                    + URLEncoder.encode(newEmployeeEntity.getInitials(), "UTF-8")
                    .replace("+", "%20") + ".jpg");
        } catch (UnsupportedEncodingException ignore) {
            log.warn("User initials can't encode to UTF-8", newEmployeeEntity.getInitials());
        }

        return newEmployeeEntity;
    }

    private EnumCategory getCategory(String category) {
        if(WORKER_CATEGORY.equals(category)) return EnumCategory.WORKER;
        if(SPEC_CATEGORY.equals(category)) return EnumCategory.SPECIALIST;
        if(LEADER_CATEGORY.equals(category)) return EnumCategory.LEADER;
        return null;
    }

    @Override
    public EmployeeEntity checkValues(XSSFRow row, EmployeeEntity object) {
        if(object == null) {
            throw new IllegalArgumentException("Прочитанное подразделение NULL! Строка: " + row.getRowNum() + 1);
        }

        if(StringUtils.isEmpty(object.getInitials()) || StringUtils.isEmpty(object.getFirstName())
                || StringUtils.isEmpty(object.getSecondName()) || StringUtils.isEmpty(object.getThirdName())
                || object.getPersonnelNumber() == null || object.getGender() == null) {
            throw new UnexpectedRequestParams("У сотрудника пустое Ф.И.О или табельный или пол! Строка: " + row.getRowNum() + 1);
        }

        return object;
    }
}
