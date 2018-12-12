package ru.kolaer.server.webportal.model.entity.typework;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 12.09.2016.
 */
@Entity
@Table(name = "type_work")
@Data
public class TypeWorkEntity implements BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
