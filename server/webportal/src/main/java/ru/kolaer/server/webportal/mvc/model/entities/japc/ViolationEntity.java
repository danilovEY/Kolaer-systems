package ru.kolaer.server.webportal.mvc.model.entities.japc;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 08.09.2016.
 */
@Entity
@Table(name = "violation")
@Data
public class ViolationEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "violation")
    private String violation;

    @Column(name = "todo")
    private String todo;

    @Column(name = "start_making")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startMaking;

    @Column(name = "date_limit_elimination")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLimitElimination;

    @Column(name = "date_end_elimination")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEndElimination;

    @Column(name = "journal_id", nullable = false)
    private Long journalId;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "executor_id", nullable = false)
    private Long executorId;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_journal", nullable = false)
    private JournalViolationEntity journalViolation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_writer", nullable = false)
    private EmployeeEntity writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_executor")
    private EmployeeEntity executor;

    @Column(name = "effective", nullable = false)
    private boolean effective;

    @Column(name = "stage", nullable = false)
    @Enumerated(EnumType.STRING)
    private StageEnum stageEnum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ViolationTypeEntity type;
}
