package ru.kolaer.api.mvp.model.kolaerweb.kolchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.List;

/**
 * Created by danilovey on 07.11.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupDto implements BaseDto {
    private Long id;
    private String name;
    private AccountDto userCreated;
    private ChatGroupType type = ChatGroupType.PRIVATE;
    private List<ChatUserDto> users;
}
