package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.List;

/**
 * Created by danilovey on 08.09.2016.
 */
public class JournalViolationBase implements JournalViolation {
    private Integer id;
    private String name;
    private GeneralDepartamentEntity departamentEntity;
    private List<Violation> violations;
    private GeneralEmployeesEntity writer;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Violation> getViolations() {
        return this.violations;
    }

    @Override
    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    @Override
    public GeneralDepartamentEntity getDepartament() {
        return this.departamentEntity;
    }

    @Override
    public void setDepartament(GeneralDepartamentEntity departament) {
        this.departamentEntity = departament;
    }

    @Override
    public GeneralEmployeesEntity getWriter() {
        return this.writer;
    }

    @Override
    public void setWriter(GeneralEmployeesEntity writer) {
        this.writer = writer;
    }
}
