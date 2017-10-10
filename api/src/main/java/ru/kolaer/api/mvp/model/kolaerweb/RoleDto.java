package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements BaseDto {
    private Long id;
    private String type;
    private Long accountId;
}
