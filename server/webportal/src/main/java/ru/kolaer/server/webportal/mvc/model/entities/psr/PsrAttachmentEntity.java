package ru.kolaer.server.webportal.mvc.model.entities.psr;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_attachment")
@Data
public class PsrAttachmentEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path_file", nullable = false)
    private String pathFile;

    @Column(name = "pst_register_id", nullable = false)
    private Long psrRegisterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psr_register_id", nullable = false)
    private PsrRegisterEntity psrRegister;

}
