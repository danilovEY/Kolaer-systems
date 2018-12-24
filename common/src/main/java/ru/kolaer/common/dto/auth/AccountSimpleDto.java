package ru.kolaer.common.dto.auth;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

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
    private boolean accessOit;
    private boolean accessUser = true;
    private boolean accessOk;
    private boolean accessVacationAdmin;
    private boolean accessVacationDepEdit;
    private boolean accessTypeWork;
}
