package ru.kolaer.server.webportal.model.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.common.dto.BaseDto;

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
    private boolean absolutePath;

    public UploadFileDto(Long id) {
        this.id = id;
    }

}
