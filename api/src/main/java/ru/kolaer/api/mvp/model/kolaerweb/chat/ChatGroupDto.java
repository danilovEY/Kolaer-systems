package ru.kolaer.api.mvp.model.kolaerweb.chat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.List;

/**
 * Created by danilovey on 07.11.2017.
 */
@Data
public class ChatGroupDto implements BaseDto {
    private Long id;
    private String name;
    private List<ChatUserDto> users;
}
