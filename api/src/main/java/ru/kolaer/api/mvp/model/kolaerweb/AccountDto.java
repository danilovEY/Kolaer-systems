package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */
@Data
public class AccountDto implements BaseDto{
    private Long id;
    private String username;
    private String password;
    private String email;
    private EmployeeDto employee;
    private boolean accessOit;
    private boolean accessUser = true;
}
