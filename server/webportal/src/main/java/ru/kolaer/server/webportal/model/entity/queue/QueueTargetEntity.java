package ru.kolaer.server.webportal.model.entity.queue;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "queue_target")
@Data
public class QueueTargetEntity implements BaseEntity {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
