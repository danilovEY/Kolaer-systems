package ru.kolaer.common.dto.auth;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.employee.EmployeeDto;

import java.security.Principal;
import java.util.Collection;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */
@Data
public class AccountDto implements BaseDto, Principal {
    private Long id;
    private String chatName;
    private String username;
    private String email;
    private String avatarUrl;
    private EmployeeDto employee;
    private boolean block;
    private Collection<String> access;

    @Override
    public String getName() {
        return username;
    }
}
