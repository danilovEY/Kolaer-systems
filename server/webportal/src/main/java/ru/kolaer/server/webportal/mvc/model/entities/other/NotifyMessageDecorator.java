package ru.kolaer.server.webportal.mvc.model.entities.other;

import io.swagger.annotations.ApiModel;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageBase;

import javax.persistence.*;

/**
 * Created by danilovey on 18.08.2016.
 */
@Entity
@Table(name = "notifications")
public class NotifyMessageDecorator implements NotifyMessage {
    private NotifyMessage notifyMessage;

    public NotifyMessageDecorator() {
        this(new NotifyMessageBase());
    }

    public NotifyMessageDecorator(NotifyMessage notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    @Id
    @SequenceGenerator(name = "notifications.seq", sequenceName = "notifications_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications.seq")
    @Column(name = "id")
    @Override
    public Integer getId() {
        return this.notifyMessage.getId();
    }

    @Override
    public void setId(Integer id) {
        this.notifyMessage.setId(id);
    }

    @Column(name = "message", nullable = false)
    @Override
    public String getMessage() {
        return this.notifyMessage.getMessage();
    }

    @Override
    public void setMessage(String message) {
        this.notifyMessage.setMessage(message);
    }
}
