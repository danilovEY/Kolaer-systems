package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by danilovey on 24.01.2017.
 */
@Data
@NoArgsConstructor
public class PostDto implements BaseDto {
    private Long id;
    private String name;
    private String abbreviatedName;
    private Integer rang;
    private TypePostEnum type;
    private String code;

    public PostDto(Long id) {
        this.id = id;
    }
}
