package ru.kolaer.server.webportal.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Created by danilovey on 30.08.2016.
 */
@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SeterProviderBean {
    private boolean isLDAP;

    public void setLDAP(boolean isLDAP) {
        this.isLDAP = isLDAP;
    }

    public boolean isLDAP() {
        return this.isLDAP;
    }
}
