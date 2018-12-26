package ru.kolaer.server.queue.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "queue_request")
@Data
public class QueueRequestEntity extends DefaultEntity {

    @Column(name = "queue_target_id", nullable = false)
    private Long queueTargetId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_target_id", insertable = false, updatable = false)
    private QueueTargetEntity target;

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
