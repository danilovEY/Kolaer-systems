package ru.kolaer.api.mvp.model.kolaerweb.webportal;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.List;

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
    /**Имеют ли доступ к URL все пользователи*/
    private List<String> accessed;
}
