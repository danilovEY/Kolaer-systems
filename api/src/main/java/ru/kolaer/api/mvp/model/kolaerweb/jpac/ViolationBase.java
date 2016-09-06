package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.Date;

/**
 * Created by danilovey on 06.09.2016.
 */
public class ViolationBase implements Violation {
    private Integer id;
    private String violation;
    private String todo;
    private Date startMakingViolation;
    private Date dateLimitEliminationViolation;
    private Date dateEndEliminationViolation;
    private GeneralEmployeesEntity writer;
    private GeneralEmployeesEntity executor;
    private Boolean effective;
    private StageEnum stage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public Date getStartMakingViolation() {
        return startMakingViolation;
    }

    public void setStartMakingViolation(Date startMakingViolation) {
        this.startMakingViolation = startMakingViolation;
    }

    public Date getDateLimitEliminationViolation() {
        return dateLimitEliminationViolation;
    }

    public void setDateLimitEliminationViolation(Date dateLimitEliminationViolation) {
        this.dateLimitEliminationViolation = dateLimitEliminationViolation;
    }

    public Date getDateEndEliminationViolation() {
        return dateEndEliminationViolation;
    }

    public void setDateEndEliminationViolation(Date dateEndEliminationViolation) {
        this.dateEndEliminationViolation = dateEndEliminationViolation;
    }

    public GeneralEmployeesEntity getWriter() {
        return writer;
    }

    public void setWriter(GeneralEmployeesEntity writer) {
        this.writer = writer;
    }

    public GeneralEmployeesEntity getExecutor() {
        return executor;
    }

    public void setExecutor(GeneralEmployeesEntity executor) {
        this.executor = executor;
    }

    @Override
    public Boolean isEffective() {
        return this.effective;
    }

    @Override
    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

    @Override
    public StageEnum getStageEnum() {
        return this.stage;
    }

    @Override
    public void setStageEnum(StageEnum stageEnum) {
        this.stage = stageEnum;
    }
}
