package ru.kolaer.api.mvp.model.kolaerweb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by danilovey on 28.07.2016.
 * Json структура для логина и пароля.
 */
@ApiModel(value = "Логин и пароль")
public class UserAndPassJson {

    @ApiModelProperty(value = "Логин", required = true)
    private String username;

    @ApiModelProperty(value = "Пароль")
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
