package ru.kolaer.server.service.chat.entity;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.server.webportal.common.entities.BaseEntity;
import ru.kolaer.server.account.entity.AccountEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
@Data
@Entity
@Table(name = "chat_message")
public class ChatMessageEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message", nullable = false, length = 4096)
    private String message;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "account_id")
    private Long accountId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_message", nullable = false)
    private Date createMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChatMessageType type = ChatMessageType.USER;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable=false, updatable=false, nullable = false)
    private AccountEntity account;

    @Column(name = "hide")
    private boolean hide;

    @ElementCollection
    @CollectionTable(name="chat_message_read", joinColumns=@JoinColumn(name="message_id"))
    @Column(name="account_id")
    private List<Long> readIds;
}
