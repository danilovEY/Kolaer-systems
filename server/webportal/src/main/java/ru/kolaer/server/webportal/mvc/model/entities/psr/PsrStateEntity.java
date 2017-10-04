package ru.kolaer.server.webportal.mvc.model.entities.psr;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_state")
@Data
public class PsrStateEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "plan")
    private boolean plan;

    @Column(name = "pst_register_id", nullable = false)
    private Long psrRegisterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psr_register_id", nullable = false)
    private PsrRegisterEntity psrRegister;
}
