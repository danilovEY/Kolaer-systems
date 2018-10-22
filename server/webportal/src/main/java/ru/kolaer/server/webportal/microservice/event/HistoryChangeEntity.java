package ru.kolaer.server.webportal.microservice.event;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_change")
@Data
public class HistoryChangeEntity implements BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value_old")
    private String valueOld;

    @Column(name = "value_new")
    private String valueNew;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event", nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryChangeEvent event = HistoryChangeEvent.UNKNOWN;

    @Column(name = "account_id")
    private Long accountId;
}
