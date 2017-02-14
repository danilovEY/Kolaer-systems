package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.system.Authentication;

import java.util.*;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultAuthentication implements Authentication {
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    private final List<AuthenticationObserver> observers = new ArrayList<>();
    private final TokenJson tokenJson;
    private boolean isAuth = false;
    private final AccountEntity ACCOUNT;

    public DefaultAuthentication() {
        final RoleEntityBase roleEntityBase = new RoleEntityBase();
        roleEntityBase.setId(1);
        roleEntityBase.setType(EnumRole.ANONYMOUS.name());

        final DepartmentEntity departmentEntity = new DepartmentEntityBase();
        departmentEntity.setId(0);
        departmentEntity.setAbbreviatedName("Подразделение");
        departmentEntity.setName("Мое подразделение");
        departmentEntity.setChiefEntity(0);

        final PostEntity postEntity = new PostEntityBase();
        postEntity.setId(0);
        postEntity.setTypeRang(TypeRangEnum.CATEGORY.getName());
        postEntity.setRang(0);
        postEntity.setName("Моя долждность");
        postEntity.setAbbreviatedName("Долждность");

        final EmployeeEntity employeeEntity = new EmployeeEntityBase();
        employeeEntity.setId(0);
        employeeEntity.setPersonnelNumber(0);
        employeeEntity.setBirthday(new Date());
        employeeEntity.setGender("Неизвестный");
        employeeEntity.setPostEntity(postEntity);
        employeeEntity.setDepartment(departmentEntity);

        this.ACCOUNT = new AccountEntityBase();
        this.ACCOUNT.setId(1);
        this.ACCOUNT.setEmail("test@test.ru");
        this.ACCOUNT.setPassword(PASSWORD);
        this.ACCOUNT.setUsername(LOGIN);
        this.ACCOUNT.setRoles(Collections.singletonList(roleEntityBase));
        this.ACCOUNT.setEmployeeEntity(employeeEntity);

        this.tokenJson = new TokenJson();
        this.tokenJson.setToken("token_test");
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson) throws ServerException {
        final boolean isLogin =  userAndPassJson != null && Optional.ofNullable(userAndPassJson.getUsername()).orElse("").equals(LOGIN)
                    && Optional.ofNullable(userAndPassJson.getPassword()).orElse("").equals(PASSWORD);
        if(isLogin) {
            this.observers.parallelStream().forEach(observer -> observer.login(this.ACCOUNT));
        }

        return isLogin;
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson, boolean remember) throws ServerException {
        return this.login(userAndPassJson);
    }

    @Override
    public AccountEntity getAuthorizedUser() {
        return this.isAuth ? this.ACCOUNT : null;
    }

    @Override
    public TokenJson getToken() {
        return this.isAuth ? this.tokenJson : null;
    }

    @Override
    public RoleEntity[] getRoles() {
        return this.isAuth
                ? this.ACCOUNT.getRoles().toArray(new RoleEntity[this.ACCOUNT.getRoles().size()])
                : new RoleEntity[0];
    }

    @Override
    public boolean isAuthentication() {
        return this.isAuth;
    }

    @Override
    public boolean logout() throws ServerException {
        this.isAuth = false;
        return true;
    }

    @Override
    public void registerObserver(AuthenticationObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(AuthenticationObserver observer) {
        this.observers.remove(observer);
    }
}
