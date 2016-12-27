package ru.kolaer.server.webportal.mvc.model.entities.japc;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolationBase;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by danilovey on 08.09.2016.
 */
@Entity
@Table(name = "violation_journal")
public class JournalViolationDecorator implements JournalViolation {
    private JournalViolation journalViolation;

    public JournalViolationDecorator() {
        this(new JournalViolationBase());
    }

    public JournalViolationDecorator(JournalViolation journalViolation) {
        this.journalViolation = journalViolation;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return journalViolation.getId();
    }

    @Override
    public void setId(Integer id) {
        this.journalViolation.setId(id);
    }

    @Column(name = "name")
    public String getName() {
        return this.journalViolation.getName();
    }

    @Override
    public void setName(String name) {
        this.journalViolation.setName(name);
    }

    @OneToMany(targetEntity = ViolationDecorator.class, cascade = CascadeType.ALL, mappedBy = "journalViolation", fetch = FetchType.LAZY)
    public List<Violation> getViolations() {
        return this.journalViolation.getViolations();
    }

    @Override
    public void setViolations(List<Violation> violations) {
        this.journalViolation.setViolations(violations);
    }

    @OneToOne(targetEntity = GeneralDepartamentEntityDecorator.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_departament")
    public GeneralDepartamentEntity getDepartament() {
        return this.journalViolation.getDepartament();
    }

    @Override
    public void setDepartament(GeneralDepartamentEntity departament) {
        this.journalViolation.setDepartament(departament);
    }
}
