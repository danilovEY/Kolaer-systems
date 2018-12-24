package ru.kolaer.common.dto.kolaerweb;

/**
 * Created by danilovey on 28.07.2016.
 * Json структура для логина и пароля.
 */
public class UserAndPassJson {

    private String username;

    private String password;

    public UserAndPassJson(){

    }

    public UserAndPassJson(String username, String password) {
        this.username = username;
        this.password = password;
    }


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
