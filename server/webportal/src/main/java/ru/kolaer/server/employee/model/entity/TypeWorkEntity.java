package ru.kolaer.server.employee.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by danilovey on 12.09.2016.
 */
@Entity
@Table(name = "type_work")
@Data
public class TypeWorkEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;
}
