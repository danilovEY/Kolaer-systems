package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by danilovey on 24.01.2017.
 */
@Data
public class PostEntityBase implements PostEntity {
    private Integer id;
    private String name;
    private String abbreviatedName;
    private String typeRang;
    private Integer rang;
    private String code;
}
