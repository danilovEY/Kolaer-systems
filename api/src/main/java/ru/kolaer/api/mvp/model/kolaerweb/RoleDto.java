package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
@Data
public class RoleDto implements BaseDto {
    private Long id;
    private String type;
    private AccountDto account;
}
