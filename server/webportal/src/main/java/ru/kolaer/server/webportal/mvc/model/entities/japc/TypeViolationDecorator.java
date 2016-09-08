package ru.kolaer.server.webportal.mvc.model.entities.japc;

import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolationBase;

import javax.persistence.*;

/**
 * Created by danilovey on 08.09.2016.
 */
@Entity
@Table(name = "violation_type")
public class TypeViolationDecorator implements TypeViolation {
    private TypeViolation typeViolation;

    public TypeViolationDecorator() {
        this(new TypeViolationBase());
    }

    public TypeViolationDecorator(TypeViolation typeViolation) {
        this.typeViolation = typeViolation;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return typeViolation.getId();
    }

    @Override
    public void setId(Integer id) {
        this.typeViolation.setId(id);
    }

    @Column(name = "name")
    public String getName() {
        return this.typeViolation.getName();
    }

    @Override
    public void setName(String name) {
        this.typeViolation.setName(name);
    }
}
