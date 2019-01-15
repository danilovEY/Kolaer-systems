package ru.kolaer.server.core.security;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface BaseRoleConstant {
    String PREFIX = "ROLE_";

    String ALL = "ALL";
    String ROLE_ALL = BaseRoleConstant.getRole(ALL);

    static String getRole(@NotNull String roleName) {
        return PREFIX + roleName;
    }

    static String hasRole(@Nullable String roleName) {
        if (roleName == null) {
            return "hasRole('ROLE_ANONYMOUS')";
        }

        return "hasRole('" + getRole(roleName) + "')";
    }

    static String hasAnyRole(@Nullable String... roleNames) {
        if (roleNames == null || roleNames.length == 0) {
            return "hasRole('ROLE_ANONYMOUS')";
        }

        return "hasAnyRole('" +
                Stream.of(roleNames)
                        .map(BaseRoleConstant::getRole)
                        .collect(Collectors.joining("','")) +
                "')";
    }
}
