package ru.kolaer.server.webportal.microservice.placement;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "placement")
@Data
public class PlacementEntity implements BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
