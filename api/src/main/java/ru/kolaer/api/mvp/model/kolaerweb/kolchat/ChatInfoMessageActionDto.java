package ru.kolaer.api.mvp.model.kolaerweb.kolchat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by danilovey on 05.02.2018.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatInfoMessageActionDto extends ChatInfoDto {
    private List<ChatMessageDto> chatMessageDtoList;
    private Long fromAccount;
}
