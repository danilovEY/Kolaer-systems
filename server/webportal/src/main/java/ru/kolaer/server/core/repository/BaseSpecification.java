package ru.kolaer.server.core.repository;

import javax.validation.constraints.NotNull;

public abstract class BaseSpecification {
    public static final String WILDCARD = "%";

    public static String containsToLowerCase(@NotNull String value) {
        return WILDCARD + value.toLowerCase() + WILDCARD;
    }
}
