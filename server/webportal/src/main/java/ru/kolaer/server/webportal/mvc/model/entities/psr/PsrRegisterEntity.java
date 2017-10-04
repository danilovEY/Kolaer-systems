package ru.kolaer.server.webportal.mvc.model.entities.psr;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_register")
@Data
public class PsrRegisterEntity implements BaseEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private EmployeeEntity author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private EmployeeEntity admin;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_open")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpen;

    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;

    @Column(name = "comment", length = 1000, nullable = false)
    private String comment;

    @OneToMany(mappedBy = "psrRegister", fetch = FetchType.LAZY)
    public List<PsrStateEntity> states;

    @OneToMany(mappedBy = "psrRegister", fetch = FetchType.LAZY)
    public List<PsrAttachmentEntity> attachments;

}
