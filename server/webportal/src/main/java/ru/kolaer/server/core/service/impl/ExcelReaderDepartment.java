package ru.kolaer.server.core.service.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.service.ExcelReader;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
public class ExcelReaderDepartment implements ExcelReader<DepartmentEntity> {
    private static String DEP_ID = "Подразделение";
    private static String DEP_SHORT_NAME_TEXT = "Текст Подразделение";
    private static String DEP_FULL_NAME_FULL_OLD = "Подразделение (полное)";
    private static String DEP_FULL_NAME_FULL_1 = "Подразделение (полное) 1";
    private static String DEP_FULL_NAME_FULL_2 = "Подразделение (полное) 2";
    private static String DEP_FULL_NAME_FULL_3 = "Подразделение (полное) 3";
    private static String DEP_FULL_NAME_FULL_4 = "Подразделение (полное) 4";

    @Override
    public DepartmentEntity parse(XSSFRow row, List<String> nameColumns) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setCode(99);

        String value = getStringValue(nameColumns, DEP_ID, row);
        departmentEntity.setExternalId(value);

        String depShortName = getStringValue(nameColumns, DEP_SHORT_NAME_TEXT, row);
        departmentEntity.setAbbreviatedName(depShortName);

        String depName = getStringValue(nameColumns, DEP_FULL_NAME_FULL_1, row);
        departmentEntity.setName(depName);

        if(depName == null) {
            depName = getStringValue(nameColumns, DEP_FULL_NAME_FULL_OLD, row);

            Pattern pattern = Pattern.compile("[а-яА-Я ]*");
            Matcher matcher = pattern.matcher(depName);

            departmentEntity.setName(depName);

            while(matcher.find()) {
                if(StringUtils.hasText(matcher.group())) {
                    departmentEntity.setAbbreviatedName(matcher.group().trim());
                    break;
                }
            }
        } else {
            String depName2 = getStringValue(nameColumns, DEP_FULL_NAME_FULL_2, row);
            String depName3 = getStringValue(nameColumns, DEP_FULL_NAME_FULL_3, row);
            String depName4 = getStringValue(nameColumns, DEP_FULL_NAME_FULL_4, row);

            departmentEntity.setName(departmentEntity.getName() + depName2 + depName3 + depName4);
        }

        if (StringUtils.isEmpty(departmentEntity.getAbbreviatedName())) {
            departmentEntity.setAbbreviatedName(depName);
        }

        log.debug("Parse department: {}", departmentEntity);

        return departmentEntity;
    }

    @Override
    public DepartmentEntity checkValues(XSSFRow row, DepartmentEntity object) {
        if(object == null) {
            throw new IllegalArgumentException("Прочитанное подразделение NULL! Строка: " + (row.getRowNum() + 1));
        }

        if(StringUtils.isEmpty(object.getName()) || StringUtils.isEmpty(object.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В подразделении пустое имя или абревиатура! Строка: " + (row.getRowNum() + 1));
        }

        return object;
    }
}
