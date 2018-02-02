package ru.kolaer.api.mvp.model.kolaerweb.kolchat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.Date;

/**
 * Created by danilovey on 08.11.2017.
 */
@Data
public class ChatInfoDto implements BaseDto {
    private Long id;
    private Long accountId;
    private ChatInfoCommand command;
    private Date createInfo;
    private String data;
}
