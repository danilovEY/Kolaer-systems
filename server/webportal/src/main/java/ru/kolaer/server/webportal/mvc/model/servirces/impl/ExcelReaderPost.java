package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.ExcelReader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
public class ExcelReaderPost implements ExcelReader<PostEntity> {
    private static String POST_CODE = "Штатная должность(Код)";
    private static String POST_NAME = "Штатная должность(Название)";

    @Override
    public PostEntity parse(XSSFRow row, List<String> nameColumns) {
        PostEntity postEntity = new PostEntity();

        String value = getStringValue(nameColumns, POST_NAME, row);
        postEntity.setName(value);
        postEntity.setAbbreviatedName(value);

        String rang = value.replaceAll("[\\D]", "");
        if (!rang.trim().isEmpty()) {
            String name = value.substring(0, value.indexOf(rang));

            postEntity.setName(name.trim());
            postEntity.setRang(Integer.valueOf(rang));
            postEntity.setType(getTypePostFromName(value));
        }

        String postCode = getStringValue(nameColumns, POST_CODE, row);
        postEntity.setCode(postCode);

        return postEntity;
    }

    private TypePostEnum getTypePostFromName(String name) {
        Pattern compile = Pattern.compile("\\d(\\s.*)");
        Matcher matcher = compile.matcher(name);
        if(matcher.find()) {
            String group = matcher.group(1).trim();
            switch (group.charAt(0)) {
                case 'р': return TypePostEnum.DISCHARGE;
                case 'к': return TypePostEnum.CATEGORY;
                case 'г': return TypePostEnum.GROUP;
                default: return null;
            }
        }

        return null;
    }

    @Override
    public PostEntity checkValues(XSSFRow row, PostEntity object) {
        if(object == null) {
            throw new IllegalArgumentException("Прочитанная должность NULL! Строка: " + row.getRowNum() + 1);
        }

        if(StringUtils.isEmpty(object.getName()) || StringUtils.isEmpty(object.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В должности пустое имя или абревиатура! Строка: " + row.getRowNum() + 1);
        }

        return object;
    }

}
