package ru.kolaer.api.mvp.model.kolaerweb.kolchat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by danilovey on 05.02.2018.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatInfoRoomActionDto extends ChatInfoDto {
    private ChatRoomDto chatRoomDto;
    private Long fromAccount;
}
