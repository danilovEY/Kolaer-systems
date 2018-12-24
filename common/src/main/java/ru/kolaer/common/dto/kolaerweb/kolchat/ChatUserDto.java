package ru.kolaer.common.dto.kolaerweb.kolchat;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

/**
 * Created by danilovey on 02.11.2017.
 */
@Data
public class ChatUserDto implements BaseDto {
    private Long id;
    private Long accountId;
    private String name;
    private String sessionId;
    private ChatUserStatus status;
}
