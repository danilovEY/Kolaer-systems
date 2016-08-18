package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.system.network.kolaerweb.NotifyMessageTable;

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotifyMessageTableImpl implements NotifyMessageTable {
    private final RestTemplate restTemplate = new RestTemplate();
    private String URL_GET_LAST;

    public NotifyMessageTableImpl(String path) {
            this.URL_GET_LAST = path + "/get/last";
    }

    @Override
    public NotifyMessage getLastNotifyMessage() throws ServerException {
        return this.restTemplate.getForObject(this.URL_GET_LAST, NotifyMessage.class);
    }
}
