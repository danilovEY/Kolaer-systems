package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.system.Authentication;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;
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
public class AuthenticationOnNetwork implements Authentication, RestTemplateService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationOnNetwork.class);
    private final List<AuthenticationObserver> authenticationObserverList;
    private final RestTemplate restTemplate;
    private final String pathToServer;
    private final ObjectMapper objectMapper;
    private AccountDto accountsEntity;
    private TokenJson tokenJson;
    private boolean isAuth = false;
    private final String URL_TO_GET_TOKEN;
    private final String URL_TO_GET_USER;

    public AuthenticationOnNetwork(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
        this.authenticationObserverList = new ArrayList<>();
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

        LOG.info("Авторизация для: {}", userAndPassJson.getUsername());

        try {
            ServerResponse<TokenJson> response = getServerResponse(restTemplate.postForEntity(URL_TO_GET_TOKEN, userAndPassJson, String.class),
                    TokenJson.class, objectMapper);
            if(response.isServerError()) {
                LOG.error("EROOR!");
            } else {
                this.tokenJson = response.getResponse();
                LOG.info("Токен получен...");
            }


            LOG.info(this.URL_TO_GET_USER + "?token=" + this.tokenJson.getToken());
            this.accountsEntity = this.restTemplate.getForObject(this.URL_TO_GET_USER + "?token=" + this.tokenJson.getToken(), AccountDto.class);
            if(this.accountsEntity != null) {
                LOG.info("Пользователь получен...");
                this.isAuth = true;

                this.notifyObserversLogin();

                LOG.info("Авторизация прошла успешно!");

                return true;
            } else {
                LOG.info("Авторизация не прошла!");
            }
        } catch (Exception ex) {
            LOG.error("Не удалось авторизоваться!", ex);
        }

        return false;
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson, boolean remember) {
        if(remember) {
            try {
                final List<String> loginAndPassList =
                        Arrays.asList(userAndPassJson.getUsername(), userAndPassJson.getPassword());

                Files.write(Paths.get(Authentication.TEMP_NAME),
                        loginAndPassList, Charset.forName("UTF-8"));
            } catch (IOException e) {
                LOG.error("Не удалось запомнить логин и пароль!", e);
                UniformSystemEditorKitSingleton.getInstance().getUISystemUS()
                        .getNotification().showErrorNotifi("Ошибка!", "Не удалось запомнить логин и пароль!");
            }
        } else {
            new File(Authentication.TEMP_NAME).delete();
        }

        return this.login(userAndPassJson);
    }

    @Override
    public boolean loginIsRemember() {
        if(new File(Authentication.TEMP_NAME).exists()) {
            try {
                final List<String> strings = Files
                        .readAllLines(Paths.get(Authentication.TEMP_NAME), Charset.forName("UTF-8"));

                final UserAndPassJson userAndPassJson = new UserAndPassJson();

                if(strings.size() > 0) {
                    userAndPassJson.setUsername(strings.get(0));
                }

                if(strings.size() > 1) {
                    userAndPassJson.setPassword(strings.get(1));
                }

                return this.login(userAndPassJson);
            } catch (IOException e) {
                LOG.error("Не удалось прочитать авто логин!", e);
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
            obs.login(this.accountsEntity);
        });
    }

    private void notifyObserversLogout() {
        this.authenticationObserverList.parallelStream().forEach(obs -> {
            obs.logout(this.accountsEntity);
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
