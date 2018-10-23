package ru.kolaer.server.webportal.microservice.storage.entity;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "upload_file")
@Entity
public class UploadFileEntity implements BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
