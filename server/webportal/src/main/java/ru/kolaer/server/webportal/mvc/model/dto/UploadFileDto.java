package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileDto implements BaseDto {
    private Long id;
    private String path;
    private String fileName;
    private LocalDateTime fileCreate;
    private Long accountId;

    public UploadFileDto(Long id) {
        this.id = id;
    }

}
