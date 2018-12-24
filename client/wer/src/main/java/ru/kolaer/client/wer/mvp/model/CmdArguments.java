package ru.kolaer.client.wer.mvp.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by danilovey on 14.03.2017.
 */
public class CmdArguments {
    public static final CmdArguments EMPTY = new CmdArguments();

    @Getter
    private String host;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter @Setter
    private int maxCountLoad;

    public CmdArguments(String host, String username, String password, int maxCountLoad) {
        this.setHost(host);
        this.setUsername(username);
        this.setPassword(password);
        this.setMaxCountLoad(maxCountLoad);
    }

    public CmdArguments() {
        this(null, null, null, 0);
    }

    public void setHost(String host) {
        this.host = host == null ? "" : host;
    }

    public void setUsername(String username) {
        this.username = username == null ? "" : username;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    @Override
    public String toString() {
        String result = this.host.isEmpty() ? "" : "/r:" + this.host;
        result += this.username.isEmpty() ? "" : " /u:" + this.username;
        result += this.password.isEmpty() ? "" : " /p:" + this.password;
        result += this.maxCountLoad == 0 ? "" : " /c:" + this.maxCountLoad;
        return result;
    }
}
