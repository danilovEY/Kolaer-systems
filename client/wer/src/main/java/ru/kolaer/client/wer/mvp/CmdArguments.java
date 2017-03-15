package ru.kolaer.client.wer.mvp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by danilovey on 14.03.2017.
 */
@AllArgsConstructor
@NoArgsConstructor
public class CmdArguments {

    @Getter
    private String host;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter @Setter
    private int maxCountLoad;

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
        result += this.username.isEmpty() ? "" : "/u:" + this.username;
        result += this.password.isEmpty() ? "" : "/p:" + this.password;
        result += this.maxCountLoad == 0 ? "" : "/c:" + this.maxCountLoad;
        return result;
    }
}
