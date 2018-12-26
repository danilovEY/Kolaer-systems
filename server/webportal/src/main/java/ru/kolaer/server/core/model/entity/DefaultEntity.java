package ru.kolaer.server.core.model.entity;

import javax.persistence.*;

/**
 * Created by danilovey on 04.10.2017.
 */
@MappedSuperclass
public abstract class DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
