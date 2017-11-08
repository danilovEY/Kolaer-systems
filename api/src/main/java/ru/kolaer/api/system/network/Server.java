package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;

/**
 * Created by Danilov on 28.07.2016.
 */
public interface Server {
    /**Получить статус сервера.*/
    ServerResponse<ServerStatus> getServerStatus();
    String getUrl();
}
