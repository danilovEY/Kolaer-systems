package ru.kolaer.server.webportal.model.entity.other;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 18.08.2016.
 */
@Entity
@Table(name = "notification")
@Data
public class NotifyMessageEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create", nullable = false)
    private Date create;
}
