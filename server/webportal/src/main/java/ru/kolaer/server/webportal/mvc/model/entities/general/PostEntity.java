package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 24.01.2017.
 */
@Entity
@Table(name = "post")
@Data
public class PostEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "abbreviated_name", nullable = false, unique = true)
    private String abbreviatedName;

    @Column(name = "code", length = 15)
    private String code;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "rang")
    private Integer rang;
}
