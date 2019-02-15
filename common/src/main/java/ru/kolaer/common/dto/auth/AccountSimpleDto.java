package ru.kolaer.common.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

import java.util.Collection;
import java.util.stream.Stream;

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

    @JsonIgnore
    public boolean hasAccess(String access) {
        return access.contains(access);
    }

    @JsonIgnore
    public boolean hasAnyAccess(String... accesses) {
        return Stream.of(accesses).anyMatch(access::contains);
    }
}
