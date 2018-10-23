package ru.kolaer.server.service.counter.entity;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by danilovey on 25.08.2016.
 */
@Entity
@Table(name = "ru/kolaer/server/service/counter")
@Data
public class CounterEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "counter_start", nullable = false)
    private LocalDateTime start;

    @Column(name = "counter_end", nullable = false)
    private LocalDateTime end;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "display_on_vacation")
    private boolean displayOnVacation;
}
