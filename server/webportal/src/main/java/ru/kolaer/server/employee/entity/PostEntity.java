package ru.kolaer.server.employee.entity;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

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
    @Enumerated(EnumType.STRING)
    private TypePostEnum type;

    @Column(name = "rang")
    private Integer rang;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "external_id")
    private String externalId;

    @Override
    public String toString() {
        return "PostEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviatedName='" + abbreviatedName + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", rang=" + rang +
                ", deleted=" + deleted +
                ", externalId='" + externalId + '\'' +
                '}';
    }
}
