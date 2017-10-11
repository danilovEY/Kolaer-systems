package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;

/**
 * Created by danilovey on 11.10.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse<T> {
    private boolean serverError;
    private ServerExceptionMessage exceptionMessage;
    private T response;

    public static <K> ServerResponse<K> createServerResponse(K object) {
        return new ServerResponse<>(false, null, object);
    }

    public static <K> ServerResponse<K> createServerResponse() {
        return new ServerResponse<>(false, null, null);
    }
}
