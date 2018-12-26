package ru.kolaer.server.ticket.model.entity;

import lombok.Data;
import ru.kolaer.server.ticket.model.TypeOperation;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "ticket")
@Data
public class TicketEntity implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

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
