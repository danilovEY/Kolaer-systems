package ru.kolaer.server.counter.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by danilovey on 25.08.2016.
 */
@Entity
@Table(name = "counter")
@Data
public class CounterEntity extends DefaultEntity {

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
