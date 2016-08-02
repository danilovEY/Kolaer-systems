package ru.kolaer.client.javafx.system.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exeptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.system.network.Authentication;
import ru.kolaer.client.javafx.tools.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danilovey on 02.08.2016.
 */
public class AuthenticationImpl implements Authentication {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationImpl.class);

    private final RestTemplate restTemplate;
    private final String pathToServer;
    private GeneralAccountsEntity accountsEntity;
    private TokenJson tokenJson;
    private boolean isAuth = false;
    private final String URL_TO_GET_TOKEN;
    private final String URL_TO_GET_USER;

    public AuthenticationImpl(StringBuilder path) {
        this.restTemplate = new RestTemplate();
        this.pathToServer = path.append("/rest").toString() + "/authentication";
        this.URL_TO_GET_TOKEN = this.pathToServer + "/token";
        this.URL_TO_GET_USER = this.pathToServer + "/user";
    }

    public boolean login(UserAndPassJson userAndPassJson) throws ServerException {
        if (userAndPassJson == null) {
            throw new IllegalArgumentException("UserAndPassJson can't be a null!");
        } else if (this.isAuth) {
            this.logout();
        }

        try {
            this.tokenJson  = this.restTemplate.postForObject(this.URL_TO_GET_TOKEN, userAndPassJson, TokenJson.class);
            this.accountsEntity = this.restTemplate.getForObject(this.URL_TO_GET_USER + "?token=" + this.tokenJson.getToken(), GeneralAccountsEntity.class);
            this.isAuth = true;
            return true;
        } catch (RestClientException ex) {
            LOG.error("Не удалось авторизоваться!", ex);
            throw new ServerException(ex);
        }
    }

    public GeneralAccountsEntity getAuthorizedUser() {
        return this.accountsEntity;
    }

    public boolean isAuthentication() {
        return this.isAuth;
    }

    public boolean logout() throws ServerException {
        this.isAuth = false;
        this.accountsEntity = null;
        this.tokenJson = null;
        return true;
    }
}
