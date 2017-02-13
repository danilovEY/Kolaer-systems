package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;

/**
 * Created by danilovey on 13.02.2017.
 */
public interface TokenToHeader {
    RestTemplate restTemplate = new RestTemplate();

    default HttpEntity getTokenToHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-token", UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication().getToken().getToken());

        return new HttpEntity(headers);
    }
}
