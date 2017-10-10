package ru.kolaer.server.webportal.security;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 10.10.2017.
 */
public class RoleUtils {
    public static final String OIT = "accessOit";
    public static final String USER = "accessUser";
    public static final String ALL = "ALL";

    public static List<String> roleToListString(AccountEntity accountEntity) {
        ArrayList<String> roles = new ArrayList<>();
        if (accountEntity.isAccessOit()) {
            roles.add(OIT);
        }
        if (accountEntity.isAccessUser()) {
            roles.add(USER);
        }

        return roles;
    }

    public static List<String> roleToListString(AccountDto accountDto) {
        ArrayList<String> roles = new ArrayList<>();
        if (accountDto.isAccessOit()) {
            roles.add(OIT);
        }
        if (accountDto.isAccessUser()) {
            roles.add(USER);
        }

        return roles;
    }

    public static List<String> roleToListString(UrlSecurityEntity urlSecurityEntity) {
        ArrayList<String> roles = new ArrayList<>();
        if (urlSecurityEntity.isAccessOit()) {
            roles.add(OIT);
        }
        if (urlSecurityEntity.isAccessAll()) {
            roles.add(ALL);
        }
        if (urlSecurityEntity.isAccessUser()) {
            roles.add(USER);
        }
        return roles;
    }

    public static List<String> roleToListString(UrlSecurityDto urlSecurityDto) {
        ArrayList<String> roles = new ArrayList<>();
        if (urlSecurityDto.isAccessAll()) {
            roles.add(ALL);
        }
        if (urlSecurityDto.isAccessOit()) {
            roles.add(OIT);
        }
        if (urlSecurityDto.isAccessUser()) {
            roles.add(USER);
        }
        return roles;
    }

}
