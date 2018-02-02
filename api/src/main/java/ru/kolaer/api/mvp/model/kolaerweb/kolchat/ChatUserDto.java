package ru.kolaer.api.mvp.model.kolaerweb.kolchat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

/**
 * Created by danilovey on 02.11.2017.
 */
@Data
public class ChatUserDto implements BaseDto {
    private Long id;
    private Long accountId;
    private String name;
    private String sessionId;
}
