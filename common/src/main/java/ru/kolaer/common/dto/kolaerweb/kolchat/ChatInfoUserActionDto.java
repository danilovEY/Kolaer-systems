package ru.kolaer.common.dto.kolaerweb.kolchat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by danilovey on 05.02.2018.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatInfoUserActionDto extends ChatInfoDto {
    private ChatUserDto chatUserDto;
}
