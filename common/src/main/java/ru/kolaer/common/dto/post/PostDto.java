package ru.kolaer.common.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.TypePostEnum;

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
