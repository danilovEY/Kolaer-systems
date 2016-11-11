package ru.kolaer.server.webportal.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

/**
 * Created by danilovey on 11.11.2016.
 */
@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionInfo {
    private GeneralAccountsEntity generalAccountsEntity;

    public UserSessionInfo() {

    }

    public GeneralAccountsEntity getGeneralAccountsEntity() {
        return generalAccountsEntity;
    }

    public void setGeneralAccountsEntity(GeneralAccountsEntity generalAccountsEntity) {
        this.generalAccountsEntity = generalAccountsEntity;
    }
}
