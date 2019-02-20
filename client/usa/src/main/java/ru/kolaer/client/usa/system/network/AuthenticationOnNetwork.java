package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.observers.AuthenticationObserver;
import ru.kolaer.client.core.system.Authentication;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.system.SettingsSingleton;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.TokenJson;
import ru.kolaer.common.dto.kolaerweb.UserAndPassJson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 02.08.2016.
 */
@Slf4j
public class AuthenticationOnNetwork implements Authentication, RestTemplateService {
    private final List<AuthenticationObserver> authenticationObserverList = new ArrayList<>();
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private AccountDto accountsEntity;
    private TokenJson tokenJson;
    private boolean isAuth = false;
    private final String PATH_REMEMBER_TOKEN;
    private final String URL_TO_GET_TOKEN;
    private final String URL_TO_GET_USER;

    public AuthenticationOnNetwork(RestTemplate restTemplate, ObjectMapper objectMapper, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.PATH_REMEMBER_TOKEN = SettingsSingleton.getInstance().getPathCache() + "\\remember_token.txt";
        this.URL_TO_GET_TOKEN = path + "/authentication/login";
        this.URL_TO_GET_USER = path + "/user/employee";
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
            return readToken(response.getResponse());
        }

        return false;
    }

    private boolean readToken(TokenJson token) {
        if (token == null) {
            throw new IllegalArgumentException("Token can't be a null!");
        } else if (this.isAuth) {
            this.logout();
        }

        this.tokenJson = token;
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

        return false;
    }

    @Override
    public boolean login(UserAndPassJson userAndPassJson, boolean remember) {
        boolean isLogin = login(userAndPassJson);

        if(remember) {
            try {
                Files.write(
                        Paths.get(PATH_REMEMBER_TOKEN),
                        Collections.singletonList(tokenJson.getToken()),
                        Charset.forName("UTF-8")
                );
            } catch (IOException e) {
                log.error("Не удалось запомнить логин и пароль!", e);
                UniformSystemEditorKitSingleton.getInstance().getUISystemUS()
                        .getNotification().showErrorNotify("Ошибка!", "Не удалось запомнить логин и пароль!");
            }
        } else {
            new File(PATH_REMEMBER_TOKEN).delete();
        }

        return isLogin;
    }

    @Override
    public boolean loginIsRemember() {
        if(new File(PATH_REMEMBER_TOKEN).exists()) {
            try {
                return Files.readAllLines(Paths.get(PATH_REMEMBER_TOKEN), Charset.forName("UTF-8"))
                        .stream()
                        .findFirst()
                        .map(TokenJson::new)
                        .map(this::readToken)
                        .orElse(false);
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
