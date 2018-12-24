package ru.kolaer.common.dto.kolaerweb.kolpass;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

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
