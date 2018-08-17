package ru.kolaer.server.webportal.security;

import ru.kolaer.api.mvp.model.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 10.10.2017.
 */
public class RoleUtils {
    public static final String OIT = "accessOit";
    public static final String OK = "accessOk";
    public static final String USER = "accessUser";
    public static final String ALL = "ALL";
    public static final String VACATION_ADMIN = "accessVacationAdmin";
    public static final String VACATION_DEP_EDIT = "accessVacationDepEdit";

    public static List<String> roleToListString(AccountEntity accountEntity) {
        ArrayList<String> roles = new ArrayList<>();
        if (accountEntity.isAccessOit()) {
            roles.add(OIT);
        }
        if (accountEntity.isAccessUser()) {
            roles.add(USER);
        }
        if (accountEntity.isAccessOk()) {
            roles.add(OK);
        }
        if (accountEntity.isAccessVacationAdmin()) {
            roles.add(VACATION_ADMIN);
        }
        if (accountEntity.isAccessVacationDepEdit()) {
            roles.add(VACATION_DEP_EDIT);
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
        if (urlSecurityDto.isAccessOk()) {
            roles.add(OK);
        }
        if (urlSecurityDto.isAccessVacationAdmin()) {
            roles.add(VACATION_ADMIN);
        }
        if (urlSecurityDto.isAccessVacationDepEdit()) {
            roles.add(VACATION_DEP_EDIT);
        }
        return roles;
    }

}
