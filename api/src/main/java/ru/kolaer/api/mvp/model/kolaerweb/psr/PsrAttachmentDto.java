package ru.kolaer.api.mvp.model.kolaerweb.psr;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

/**
 * Created by danilovey on 29.07.2016.
 */
@Data
public class PsrAttachmentDto implements BaseDto {
    private Long id;
    private String pathFile;
    private String name;
    private PsrRegisterDto psrRegister;
}
