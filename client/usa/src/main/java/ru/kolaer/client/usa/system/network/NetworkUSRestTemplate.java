package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.NetworkUS;
import ru.kolaer.client.core.system.network.OtherPublicAPI;
import ru.kolaer.client.core.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.client.usa.system.SettingsSingleton;
import ru.kolaer.client.usa.system.network.kolaerweb.KolaerWebServerImpl;

import java.nio.charset.Charset;

/**
 * Реализация интерфейса для работы с сетью.
 *
 * @author danilovey
 * @version 0.1
 */
public class NetworkUSRestTemplate implements NetworkUS {
    private final RestTemplate globalRestTemplate;
    private final KolaerWebServer kolaerWebServer;
    private final OtherPublicAPI otherPublicAPI;

    public NetworkUSRestTemplate(ObjectMapper objectMapper) {
        this.globalRestTemplate = new RestTemplate();
        this.globalRestTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        this.globalRestTemplate.setErrorHandler(new ResponseErrorHandlerNotifications(objectMapper));

        this.kolaerWebServer = new KolaerWebServerImpl(objectMapper, globalRestTemplate,
                SettingsSingleton.getInstance().getUrlServerMain(),
                SettingsSingleton.getInstance().getUrlServiceChat());

        this.otherPublicAPI = new OtherPublicAPIImpl(objectMapper, globalRestTemplate);
    }

    @Override
    public KolaerWebServer getKolaerWebServer() {
        return this.kolaerWebServer;
    }

    @Override
    public OtherPublicAPI getOtherPublicAPI() {
        return this.otherPublicAPI;
    }

    public RestTemplate getGlobalRestTemplate() {
        return globalRestTemplate;
    }
}
