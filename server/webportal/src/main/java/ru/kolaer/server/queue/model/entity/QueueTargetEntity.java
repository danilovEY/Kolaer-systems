package ru.kolaer.server.queue.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "queue_target")
@Data
public class QueueTargetEntity extends DefaultEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
