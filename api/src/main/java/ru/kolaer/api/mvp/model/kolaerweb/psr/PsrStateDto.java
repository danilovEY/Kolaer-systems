package ru.kolaer.api.mvp.model.kolaerweb.psr;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@Data
public class PsrStateDto implements BaseDto {
    private Long id;
    private String comment;
    private Date date;
    private boolean plan;
    private Long psrRegisterId;
}
