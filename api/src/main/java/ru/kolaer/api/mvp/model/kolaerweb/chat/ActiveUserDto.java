package ru.kolaer.api.mvp.model.kolaerweb.chat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

/**
 * Created by danilovey on 02.11.2017.
 */
@Data
public class ActiveUserDto implements BaseDto {
    private Long id;
    private String roomName;
    private String name;
}
