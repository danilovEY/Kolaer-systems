package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
public class PasswordRepositoryDto implements BaseDto {
    private Long id;
    private String name;
    private Long accountId;
    private String urlImage;
}
