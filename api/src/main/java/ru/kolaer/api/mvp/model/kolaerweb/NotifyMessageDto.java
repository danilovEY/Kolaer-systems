package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by danilovey on 18.08.2016.
 */
@Data
public class NotifyMessageDto implements BaseDto {
    private Long id;
    private String message;
}
