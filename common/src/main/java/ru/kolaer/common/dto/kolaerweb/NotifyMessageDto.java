package ru.kolaer.common.dto.kolaerweb;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

import java.util.Date;

/**
 * Created by danilovey on 18.08.2016.
 */
@Data
public class NotifyMessageDto implements BaseDto {
    private Long id;
    private String message;
    private Date create;
}
