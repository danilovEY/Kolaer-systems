package ru.kolaer.server.webportal.mvc.model;

/**
 * Created by Danilov on 24.07.2016.
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
