package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

import java.util.List;

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
    private List<RoleDto> roles;
    private EmployeeDto employee;
}
