package ru.kolaer.server.notification.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 18.08.2016.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notification")
@Data
public class NotifyMessageEntity extends DefaultEntity {

    @Column(name = "message", nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create", nullable = false)
    private Date create;
}
