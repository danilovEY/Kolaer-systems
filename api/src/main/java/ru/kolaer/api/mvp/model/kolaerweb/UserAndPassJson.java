package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 28.07.2016.
 * Json структура для логина и пароля.
 */
public class UserAndPassJson {
    private String username;
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
