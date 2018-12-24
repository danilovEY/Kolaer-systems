package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import ru.kolaer.common.dto.error.ServerExceptionMessage;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.system.ui.NotificationUS;

import java.io.IOException;

/**
 * Created by danilovey on 15.02.2017.
 */
public class ResponseErrorHandlerNotifications extends DefaultResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseErrorHandlerNotifications.class);
    private final ObjectMapper objectMapper;

    public ResponseErrorHandlerNotifications(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        final NotificationUS notification = UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS().getNotification();

        switch (response.getStatusCode().value()) {
            case 400 : notification
                    .showErrorNotify("Неверные данные!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;

            case 403: notification
                    .showErrorNotify("Нет доступа!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;

            case 503: notification
                    .showErrorNotify("Ошибка на сервере!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;

            default: notification
                    .showErrorNotify("Ошибка запроса!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;
        }

        super.handleError(response);
    }

    private ServerExceptionMessage getExceptionMessageRequest(ClientHttpResponse response) throws IOException {
        return this.objectMapper.readValue(response.getBody(), ServerExceptionMessage.class);
    }

    private String getMessage(ServerExceptionMessage ex) {
        log.error("Status: \"{}\". Message: \"{}\"",ex.getStatus(), ex.getMessage());
        return ex.getMessage();
    }
}
