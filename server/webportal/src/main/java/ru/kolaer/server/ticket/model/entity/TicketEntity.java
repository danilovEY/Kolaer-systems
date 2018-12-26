package ru.kolaer.server.ticket.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.ticket.model.TypeOperation;

import javax.persistence.*;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "ticket")
@Data
public class TicketEntity extends DefaultEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", insertable = false, updatable = false)
    private BankAccountEntity bankAccount;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "register_id", nullable = false)
    private Long registerId;

    @Column(name = "bank_account_id", nullable = false)
    private Long bankAccountId;

    @Column(name = "type_operation", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOperation typeOperation;

}
