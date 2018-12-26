package ru.kolaer.server.core.model.entity.historychange;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "history_change")
@Data
public class HistoryChangeEntity extends DefaultEntity {

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
