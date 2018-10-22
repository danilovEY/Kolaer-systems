package ru.kolaer.client.usa.system.network.kolaerweb;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.system.Authentication;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;

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

        Authentication authentication = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication();
        if(authentication.isAuthentication()) {
            headers.set("x-token", authentication.getToken().getToken());
        }

        return headers;
    }

    default HttpEntity<?> getHeader() {
        return new HttpEntity<>(getTokenToHeader());
    }

    default HttpEntity<?> getHeader(Object obj) {
        return new HttpEntity<>(obj, getTokenToHeader());
    }
}
