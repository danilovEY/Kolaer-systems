package ru.kolaer.common.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.employee.EnumGender;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.common.dto.kolaerweb.TokenJson;
import ru.kolaer.common.dto.kolaerweb.TypePostEnum;
import ru.kolaer.common.dto.kolaerweb.UserAndPassJson;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.common.observers.AuthenticationObserver;
import ru.kolaer.common.system.Authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        this.ACCOUNT.setUsername(LOGIN);
        this.ACCOUNT.setEmployee(employee);

        this.tokenJson = new TokenJson();
        this.tokenJson.setToken("token_test");
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson) {
        final boolean isLogin =  userAndPassJson != null && Optional.ofNullable(userAndPassJson.getUsername()).orElse("").equals(LOGIN)
                    && Optional.ofNullable(userAndPassJson.getPassword()).orElse("").equals(PASSWORD);
        if(isLogin) {
            this.observers.parallelStream().forEach(observer -> observer.login(this.ACCOUNT));
        }

        return isLogin;
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson, boolean remember) {
        return this.login(userAndPassJson);
    }

    @Override
    public boolean loginIsRemember() {
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
    public boolean isAuthentication() {
        return this.isAuth;
    }

    @Override
    public boolean logout() {
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
