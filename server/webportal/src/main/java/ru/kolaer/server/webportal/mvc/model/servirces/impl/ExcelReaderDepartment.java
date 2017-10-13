package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.ExcelReader;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
public class ExcelReaderDepartment implements ExcelReader<DepartmentEntity> {
    private static String DEP_ID = "Подразделение";
    private static String DEP_NAME = "Текст Подразделение";

    @Override
    public DepartmentEntity parse(XSSFRow row, List<String> nameColumns) {
        String value = getStringValue(nameColumns, DEP_ID, row);
        Long idDep = Optional.ofNullable(value).map(Long::valueOf).orElse(null);

        DepartmentEntity departmentEntity = new DepartmentEntity();
        //departmentEntity.setId(idDep);

        String depName = row.getCell(nameColumns.indexOf(DEP_NAME)).getStringCellValue();

        Pattern pattern = Pattern.compile("[а-яА-Я ]*");
        Matcher matcher = pattern.matcher(depName);

        departmentEntity.setName(depName);
        if (matcher.find())
            departmentEntity.setAbbreviatedName(matcher.group().trim());
        else
            departmentEntity.setAbbreviatedName(depName);

        return departmentEntity;
    }

    @Override
    public DepartmentEntity checkValues(XSSFRow row, DepartmentEntity object) {
        if(object == null) {
            throw new IllegalArgumentException("Прочитанное подразделение NULL! Строка: " + row.getRowNum());
        }

        if(StringUtils.isEmpty(object.getName()) || StringUtils.isEmpty(object.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В подразделении пустое имя или абревиатура! Строка: " + row.getRowNum());
        }

        return object;
    }
}
