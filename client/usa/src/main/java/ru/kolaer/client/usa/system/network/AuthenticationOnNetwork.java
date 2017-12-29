package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.tools.Resources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 02.08.2016.
 */
@Slf4j
public class AuthenticationOnNetwork implements Authentication, RestTemplateService {
    private final String TEMP_NAME = Resources.CACHE_PATH + "\\remember_login.txt";
    private final List<AuthenticationObserver> authenticationObserverList = new ArrayList<>();
    private final RestTemplate restTemplate;
    private final String pathToServer;
    private final ObjectMapper objectMapper;
    private AccountDto accountsEntity;
    private TokenJson tokenJson;
    private boolean isAuth = false;
    private final String URL_TO_GET_TOKEN;
    private final String URL_TO_GET_USER;

    public AuthenticationOnNetwork(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.pathToServer = "http://" + Resources.URL_TO_PUBLIC_SERVER + "/rest/authentication";
        this.URL_TO_GET_TOKEN = this.pathToServer + "/login";
        this.URL_TO_GET_USER = "http://" + Resources.URL_TO_PUBLIC_SERVER + "/rest/user/get";
    }

    public boolean login(UserAndPassJson userAndPassJson) {
        if (userAndPassJson == null) {
            throw new IllegalArgumentException("UserAndPassJson can't be a null!");
        } else if (this.isAuth) {
            this.logout();
        }

        userAndPassJson.setUsername(userAndPassJson.getUsername().toLowerCase());
        userAndPassJson.setPassword(Optional.ofNullable(userAndPassJson.getPassword()).orElse(""));

        log.info("Авторизация для: {}", userAndPassJson.getUsername());

        ServerResponse<TokenJson> response = postServerResponse(restTemplate, URL_TO_GET_TOKEN, userAndPassJson,
                TokenJson.class, objectMapper);
        if(response.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(response.getExceptionMessage());
        } else {
            this.tokenJson = response.getResponse();
            log.info("Токен получен: {}", tokenJson);

            ServerResponse<AccountDto> accountServerResponse = getServerResponse(restTemplate, URL_TO_GET_USER +
                            "?token=" + this.tokenJson.getToken(),
                    AccountDto.class,
                    objectMapper);

            if(accountServerResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(accountServerResponse.getExceptionMessage());
            } else {
                accountsEntity = accountServerResponse.getResponse();
                log.info("Пользователь получен: {}", accountsEntity);
                isAuth = true;
                notifyObserversLogin();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson, boolean remember) {
        if(remember) {
            try {
                final List<String> loginAndPassList =
                        Arrays.asList(userAndPassJson.getUsername(), userAndPassJson.getPassword());

                Files.write(Paths.get(TEMP_NAME),
                        loginAndPassList, Charset.forName("UTF-8"));
            } catch (IOException e) {
                log.error("Не удалось запомнить логин и пароль!", e);
                UniformSystemEditorKitSingleton.getInstance().getUISystemUS()
                        .getNotification().showErrorNotify("Ошибка!", "Не удалось запомнить логин и пароль!");
            }
        } else {
            new File(TEMP_NAME).delete();
        }

        return this.login(userAndPassJson);
    }

    @Override
    public boolean loginIsRemember() {
        if(new File(TEMP_NAME).exists()) {
            try {
                final List<String> strings = Files
                        .readAllLines(Paths.get(TEMP_NAME), Charset.forName("UTF-8"));

                final UserAndPassJson userAndPassJson = new UserAndPassJson();

                if(strings.size() > 0) {
                    userAndPassJson.setUsername(strings.get(0));
                }

                if(strings.size() > 1) {
                    userAndPassJson.setPassword(strings.get(1));
                }

                return this.login(userAndPassJson);
            } catch (IOException e) {
                log.error("Не удалось прочитать авто логин!", e);
            }
        }
        return false;
    }

    public AccountDto getAuthorizedUser() {
        return this.accountsEntity;
    }

    @Override
    public TokenJson getToken() {
        return this.tokenJson;
    }

    public boolean isAuthentication() {
        return this.isAuth;
    }

    public boolean logout() {
        this.isAuth = false;
        this.accountsEntity = null;
        this.tokenJson = null;

        this.notifyObserversLogout();

        return true;
    }

    private void notifyObserversLogin() {
        this.authenticationObserverList.parallelStream().forEach(obs -> {
            try {
                obs.login(this.accountsEntity);
            } catch (Exception ex) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(ex);
            }
        });
    }

    private void notifyObserversLogout() {
        this.authenticationObserverList.parallelStream().forEach(obs -> {
            try {
                obs.logout(this.accountsEntity);
            } catch (Exception ex) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(ex);
            }
        });
    }

    @Override
    public void registerObserver(AuthenticationObserver observer) {
        if(observer != null)
            this.authenticationObserverList.add(observer);
    }

    @Override
    public void removeObserver(AuthenticationObserver observer) {
        if(observer != null)
            this.authenticationObserverList.remove(observer);
    }
}
