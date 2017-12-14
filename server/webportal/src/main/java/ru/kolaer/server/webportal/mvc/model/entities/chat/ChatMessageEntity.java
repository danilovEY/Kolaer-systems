package ru.kolaer.server.webportal.mvc.model.entities.chat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "room", nullable = false)
    private String room;

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
}
