package ru.kolaer.common.dto.auth;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

import java.util.Collection;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */
@Data
public class AccountSimpleDto implements BaseDto {
    private Long id;
    private String chatName;
    private String username;
    private String email;
    private String avatarUrl;
    private Long employeeId;
    private Collection<String> access;

}
