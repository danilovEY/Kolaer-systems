package ru.kolaer.server.webportal.mvc.model.entities.chat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 08.11.2017.
 */
@Data
@Entity
@Table(name = "chat_info")
public class ChatInfoEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "command", nullable = false)
    private ChatInfoCommand command;

    @Column(name = "account_id")
    private Long accountId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_info", nullable = false)
    private Date createInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable=false, updatable=false)
    private AccountEntity account;

    @Column(name = "data")
    private String data;

    @Column(name = "to_account_id")
    private Long toAccountId;
}
