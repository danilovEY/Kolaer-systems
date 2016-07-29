package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by Danilov on 24.07.2016.
 * Json структура для токена.
 */
public class TokenJson {
    private String token;

    public TokenJson() {}

    public TokenJson(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
