package ru.kolaer.server.webportal.mvc.model.entities.japc;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 08.09.2016.
 */
@Entity
@Table(name = "violation_type")
@Immutable
@Data
public class ViolationTypeEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
