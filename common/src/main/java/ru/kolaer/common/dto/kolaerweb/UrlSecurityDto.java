package ru.kolaer.common.dto.kolaerweb;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

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
    private boolean accessVacationAdmin;
    private boolean accessVacationDepEdit;
    private boolean accessTypeWork;
}
