package ru.kolaer.server.webportal.mvc.model.entities.other;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

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
}
