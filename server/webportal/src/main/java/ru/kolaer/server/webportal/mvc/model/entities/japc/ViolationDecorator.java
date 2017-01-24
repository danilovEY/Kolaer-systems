package ru.kolaer.server.webportal.mvc.model.entities.japc;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 08.09.2016.
 */
@Entity
@Table(name = "violations")
public class ViolationDecorator implements Violation {
    private Violation violation;

    public ViolationDecorator() {
        this(new ViolationBase());
    }

    public ViolationDecorator(Violation violation) {
        this.violation = violation;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.violation.getId();
    }

    @Override
    public void setId(Integer id) {
        this.violation.setId(id);
    }

    @Column(name = "violation")
    public String getViolation() {
        return this.violation.getViolation();
    }

    @Override
    public void setViolation(String violation) {
        this.violation.setViolation(violation);
    }

    @Column(name = "todo")
    public String getTodo() {
        return this.violation.getTodo();
    }

    @Override
    public void setTodo(String todo) {
        this.violation.setTodo(todo);
    }

    @Column(name = "start_making_violation")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartMakingViolation() {
        return this.violation.getStartMakingViolation();
    }

    @Override
    public void setStartMakingViolation(Date startMakingViolation) {
        this.violation.setStartMakingViolation(startMakingViolation);
    }

    @Column(name = "date_limit_elimination_violation")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateLimitEliminationViolation() {
        return this.violation.getDateLimitEliminationViolation();
    }

    @Override
    public void setDateLimitEliminationViolation(Date dateLimitEliminationViolation) {
        this.violation.setDateLimitEliminationViolation(dateLimitEliminationViolation);
    }

    @Column(name = "date_end_elimination_violation")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateEndEliminationViolation() {
        return this.violation.getDateEndEliminationViolation();
    }

    @Override
    public void setDateEndEliminationViolation(Date dateEndEliminationViolation) {
        this.violation.setDateEndEliminationViolation(dateEndEliminationViolation);
    }

    @ManyToOne(targetEntity = JournalViolationDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_journal", nullable = false)
    public JournalViolation getJournalViolation() {
        return this.violation.getJournalViolation();
    }

    @Override
    public void setJournalViolation(JournalViolation journalViolation) {
        this.violation.setJournalViolation(journalViolation);
    }

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_writer", nullable = false)
    public EmployeeEntity getWriter() {
        return this.violation.getWriter();
    }

    @Override
    public void setWriter(EmployeeEntity writer) {
        this.violation.setWriter(writer);
    }

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_executor", nullable = true)
    public EmployeeEntity getExecutor() {
        return this.violation.getExecutor();
    }

    @Override
    public void setExecutor(EmployeeEntity executor) {
        this.violation.setExecutor(executor);
    }

    @Column(name = "is_effective", nullable = false)
    public Boolean isEffective() {
        return this.violation.isEffective();
    }

    @Override
    public void setEffective(Boolean effective) {
        this.violation.setEffective(effective);
    }

    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    public StageEnum getStageEnum() {
        return this.violation.getStageEnum();
    }

    @Override
    public void setStageEnum(StageEnum stageEnum) {
        this.violation.setStageEnum(stageEnum);
    }

    @OneToOne(targetEntity = TypeViolationDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_type_violation", nullable = true)
    public TypeViolation getTypeViolation() {
        return this.violation.getTypeViolation();
    }

    @Override
    public void setTypeViolation(TypeViolation typeViolation) {
        this.violation.setTypeViolation(typeViolation);
    }
}
