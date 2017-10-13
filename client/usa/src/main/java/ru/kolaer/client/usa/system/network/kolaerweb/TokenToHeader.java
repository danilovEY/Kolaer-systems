package ru.kolaer.client.usa.system.network.kolaerweb;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

/**
 * Created by danilovey on 13.02.2017.
 */
public interface TokenToHeader {
    default <T> ParameterizedTypeReference<Page<T>> getTypeFromPage(Class<T> cls) {
        return new ParameterizedTypeReference<Page<T>>() {};
    }

    default HttpHeaders getTokenToHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-token", UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication().getToken().getToken());

        return headers;
    }
}
