package ru.kolaer.api.system.network;

/**
 * Created by Danilov on 28.07.2016.
 */
public interface Server {
    /**Получить статус сервера.*/
    ServerStatus getServerStatus();
}
