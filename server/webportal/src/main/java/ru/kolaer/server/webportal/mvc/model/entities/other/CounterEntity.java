package ru.kolaer.server.webportal.mvc.model.entities.other;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 25.08.2016.
 */
@Entity
@Table(name = "counter")
@Data
public class CounterEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "counter_start", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column(name = "counter_end", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;
}
