package ru.kolaer.server.upload.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "upload_file")
@Entity
public class UploadFileEntity extends DefaultEntity {

    @Column(name = "path", unique = true, nullable = false)
    private String path;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_create", nullable = false)
    private LocalDateTime fileCreate;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "absolute_path")
    private boolean absolutePath;

}
