package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
public class PasswordHistoryDto implements BaseDto {
    private Long id;
    private String login;
    private String password;
    private LocalDateTime passwordWriteDate;
    private LocalDateTime deadline;
}
