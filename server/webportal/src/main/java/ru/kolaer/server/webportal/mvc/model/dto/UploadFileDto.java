package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.time.LocalDateTime;

@Data
public class UploadFileDto implements BaseDto {
    private Long id;
    private String path;
    private String name;
    private LocalDateTime create;
    private Long accountId;

}
