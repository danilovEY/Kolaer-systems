package ru.kolaer.server.placement.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "placement")
@Data
public class PlacementEntity extends DefaultEntity {

    @Column(name = "name", nullable = false)
    private String name;

}
