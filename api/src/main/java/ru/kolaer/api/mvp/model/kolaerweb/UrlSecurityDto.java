package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
@Data
public class UrlSecurityDto implements BaseDto {
    private Long id;
    private String url;
    private String description;
    private String requestMethod;
    private boolean accessOit;
    private boolean accessAll;
    private boolean accessUser = true;
    private boolean accessOk;
}
