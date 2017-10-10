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
    private final AccountDto ACCOUNT;

    public DefaultAuthentication() {
        final RoleDto roleDto = new RoleDto();
        roleDto.setType(EnumRole.ANONYMOUS.name());

        final DepartmentDto department = new DepartmentDto();
        department.setAbbreviatedName("Подразделение");
        department.setName("Мое подразделение");

        final PostDto post = new PostDto();
        post.setType(TypePostEnum.CATEGORY);
        post.setRang(0);
        post.setName("Моя долждность");
        post.setAbbreviatedName("Долждность");

        final EmployeeDto employee = new EmployeeDto();
        employee.setBirthday(new Date());
        employee.setGender(EnumGender.MALE);
        employee.setPost(post);
        employee.setDepartment(department);

        this.ACCOUNT = new AccountDto();
        this.ACCOUNT.setEmail("test@test.ru");
        this.ACCOUNT.setPassword(PASSWORD);
        this.ACCOUNT.setUsername(LOGIN);
        this.ACCOUNT.setRoles(Collections.singletonList(roleDto));
        this.ACCOUNT.setEmployee(employee);

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
    public boolean loginIsRemember() throws ServerException {
        return this.login(new UserAndPassJson(LOGIN, PASSWORD));
    }

    @Override
    public AccountDto getAuthorizedUser() {
        return this.isAuth ? this.ACCOUNT : null;
    }

    @Override
    public TokenJson getToken() {
        return this.isAuth ? this.tokenJson : null;
    }

    @Override
    public RoleDto[] getRoles() {
        return this.isAuth
                ? this.ACCOUNT.getRoles().toArray(new RoleDto[this.ACCOUNT.getRoles().size()])
                : new RoleDto[0];
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
