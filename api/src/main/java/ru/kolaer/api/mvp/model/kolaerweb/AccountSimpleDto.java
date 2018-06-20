package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */
@Data
public class AccountSimpleDto implements BaseDto{
    private Long id;
    private String chatName;
    private String username;
    private String email;
    private String avatarUrl;
    private Long employeeId;
    private boolean accessOit;
    private boolean accessUser = true;
    private boolean accessOk;
}
