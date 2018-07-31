package ru.kolaer.server.webportal.mvc.model.entities.queue;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue_request")
@Data
public class QueueRequestEntity implements BaseEntity {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "queue_target_id", nullable = false)
    private Long queueTargetId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "queue_from", nullable = false)
    private LocalDateTime queueFrom;

    @Column(name = "queue_to", nullable = false)
    private LocalDateTime queueTo;

    @Column(name = "type", nullable = false)
    private QueueType type = QueueType.ONCE;
}
