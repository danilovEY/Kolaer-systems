package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.system.network.kolaerweb.NotifyMessageTable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotifyMessageTableImpl implements NotifyMessageTable {
    private final RestTemplate restTemplate;
    private String URL_GET_LAST;
    private String URL_ADD;

    public NotifyMessageTableImpl(RestTemplate globalRestTemplate, String path) {
        this.restTemplate = globalRestTemplate;
        this.URL_GET_LAST = path + "/get/last";
        this.URL_ADD = path + "/add";
    }

    @Override
    public NotifyMessage getLastNotifyMessage() throws ServerException {
        return this.restTemplate.getForObject(this.URL_GET_LAST, NotifyMessage.class);
    }

    @Override
    public void addNotifyMessage(NotifyMessage notifyMessage) throws ServerException {
        this.restTemplate.postForObject(this.URL_ADD + "?token=" + UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken(), notifyMessage, NotifyMessage.class);
    }
}
