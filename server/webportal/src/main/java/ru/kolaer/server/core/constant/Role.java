package ru.kolaer.server.core.constant;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    ADMIN,
    OK,
    USER,
    ALL,
    VACATION_ADMIN,
    VACATION_DEP_EDIT,
    TYPE_WORK_DEP_EDIT;

    public String getRole() {
        return "ROLE_" + this.name();
    }

    public static String hasRole(Role role) {
        if (role == null) {
            return "hasRole('ANONYMOUS')";
        }

        return "hasRole('" + role.name() + "')";
    }

    public static String hasRole(Role... roles) {
        if (roles == null || roles.length == 0) {
            return "hasRole('ANONYMOUS')";
        }

        return "hasAnyRole('" + Stream.of(roles).map(Enum::name).collect(Collectors.joining("','")) + "')";
    }
}
