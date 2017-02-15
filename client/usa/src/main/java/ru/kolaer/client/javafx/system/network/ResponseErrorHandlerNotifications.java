package ru.kolaer.client.javafx.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import ru.kolaer.api.mvp.model.kolaerweb.ExceptionMessageRequest;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;

import java.io.IOException;

/**
 * Created by danilovey on 15.02.2017.
 */
public class ResponseErrorHandlerNotifications extends DefaultResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseErrorHandlerNotifications.class);
    private final ObjectMapper objectMapper;

    public ResponseErrorHandlerNotifications() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        final NotificationUS notification = UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS().getNotification();


        switch (response.getStatusCode().value()) {
            case 400 : notification
                    .showErrorNotifi("Неверные данные!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;

            case 403: notification
                    .showErrorNotifi("Не доступа!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;

            case 503: notification
                    .showErrorNotifi("Ошибка на сервере!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;

            default: notification
                    .showErrorNotifi("Ошибка запроса!",
                            this.getMessage(this.getExceptionMessageRequest(response))); break;
        }

        super.handleError(response);
    }

    private ExceptionMessageRequest getExceptionMessageRequest(ClientHttpResponse response) throws IOException {
        return this.objectMapper.readValue(response.getBody(), ExceptionMessageRequest.class);
    }

    private String getMessage(ExceptionMessageRequest ex) {
        log.error("Status: \"{}\". Message: \"{}\"",ex.getStatus(), ex.getMessage());
        return ex.getMessage();
    }
}
