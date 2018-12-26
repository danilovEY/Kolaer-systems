package ru.kolaer.server.ticket.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Data
@Table(name = "ticket_register")
public class TicketRegisterEntity extends DefaultEntity {

    @Column(name = "close", nullable = false)
    private boolean close;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "create_register")
    private LocalDateTime createRegister;

    @Column(name = "send_register_time")
    private LocalDateTime sendRegisterTime;

    @Column(name = "attachment_id")
    private Long attachmentId;

    @Column(name = "include_all")
    private boolean includeAll;

}
