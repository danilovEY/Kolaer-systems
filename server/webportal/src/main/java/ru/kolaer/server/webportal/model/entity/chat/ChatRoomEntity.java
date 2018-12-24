package ru.kolaer.server.webportal.model.entity.chat;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by danilovey on 01.02.2018.
 */
@Data
@Entity
@Table(name = "chat_room")
public class ChatRoomEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "room_key", nullable = false, unique = true)
    private String roomKey;

    @Column(name = "user_created_id")
    private Long userCreatedId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChatGroupType type = ChatGroupType.PRIVATE;

    @ElementCollection
    @CollectionTable(name="chat_room_account", joinColumns=@JoinColumn(name="chat_room_id"))
    @Column(name="account_id")
    private List<Long> accountIds;
}
