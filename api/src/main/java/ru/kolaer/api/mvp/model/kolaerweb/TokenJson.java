package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Danilov on 24.07.2016.
 * Json структура для токена.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenJson {
    private String token;
}
