package ru.kolaer.common.dto.kolaerweb.kolchat;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

import java.util.Date;

/**
 * Created by danilovey on 08.11.2017.
 */
@Data
public class ChatInfoDto implements BaseDto {
    private Long id;
    private ChatInfoCommand command;
    private Date createInfo;
}
