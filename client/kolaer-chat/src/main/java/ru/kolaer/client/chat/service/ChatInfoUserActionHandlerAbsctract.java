package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;

/**
 * Created by danilovey on 05.02.2018.
 */
public abstract class ChatInfoUserActionHandlerAbsctract implements ChatInfoUserActionHandler {
    private String subId;

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        handleTransportError(session, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getNotification()
                .showErrorNotify(new Exception(exception));
    }

    @Override
    public void setSubscriptionId(String id) {
        this.subId = id;
    }

    @Override
    public String getSubscriptionId() {
        return this.subId;
    }
}
