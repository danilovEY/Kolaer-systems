package ru.kolaer.server.webportal.model.servirce.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.model.entity.general.PostEntity;
import ru.kolaer.server.webportal.model.servirce.ExcelReader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
public class ExcelReaderPost implements ExcelReader<PostEntity> {
    private static String POST_ID = "ШтатДолжность";
    private static String POST_CODE = "Код должности";
    private static String POST_SHORT_NAME = "Штатная должность";
    private static String POST_FULL_NAME_1 = "Шт.должность (полное) 1";
    private static String POST_FULL_NAME_2 = "Шт.должность (полное) 2";
    private static String POST_FULL_NAME_3 = "Шт.должность (полное) 3";
    private static String POST_FULL_NAME_4 = "Шт.должность (полное) 4";

    @Override
    public PostEntity parse(XSSFRow row, List<String> nameColumns) {
        PostEntity postEntity = new PostEntity();

//        String value = getStringValue(nameColumns, POST_ID, row);
//        postEntity.setExternalId(value);

        String value = getStringValue(nameColumns, POST_FULL_NAME_1, row);
        postEntity.setName(value);

        String postFullName2 = getStringValue(nameColumns, POST_FULL_NAME_2, row);
        String postFullName3 = getStringValue(nameColumns, POST_FULL_NAME_3, row);
        String postFullName4 = getStringValue(nameColumns, POST_FULL_NAME_4, row);
        postEntity.setName(postEntity.getName() + postFullName2 + postFullName3 + postFullName4);

        value = getStringValue(nameColumns, POST_SHORT_NAME, row);
        postEntity.setAbbreviatedName(value);


        value = postEntity.getName();

        String rang = value.replaceAll("[\\D]", "");
        if (StringUtils.hasText(rang)) {
            String name = value.substring(0, value.indexOf(rang));

            postEntity.setName(name.trim());
            postEntity.setRang(Integer.valueOf(rang));
            postEntity.setType(getTypePostFromName(value));
        }

        String postCode = getStringValue(nameColumns, POST_CODE, row);
        postEntity.setCode(postCode);

//        String externalId = Optional.ofNullable(postEntity.getCode())
//                .orElse("unknown");
//
//        if (postEntity.getRang() != null) {
//            externalId += "-" + postEntity.getRang().toString();
//        }
//
//        postEntity.setExternalId(externalId);

        log.debug("Parse post: {}", postEntity);

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
            throw new IllegalArgumentException("Прочитанная должность NULL! Строка: " + (row.getRowNum() + 1));
        }

        if(StringUtils.isEmpty(object.getName()) || StringUtils.isEmpty(object.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В должности пустое имя или абревиатура! Строка: " + (row.getRowNum() + 1));
        }

        return object;
    }

}
