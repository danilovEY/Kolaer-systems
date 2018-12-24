package ru.kolaer.common.dto.kolaerweb.kolpass;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.auth.AccountDto;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
public class PasswordRepositoryDto implements BaseDto {
    private Long id;
    private String name;
    private AccountDto account;
    private String urlImage;
    private boolean empty;
}
