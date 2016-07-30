package ru.kolaer.api.system.network.restful;

import ru.kolaer.api.system.network.Server;

/**
 * Created by Danilov on 28.07.2016.
 */
public interface RestfulServer extends Server {
    /**Получить объект для работы с БД.*/
    KolaerDataBase getKolaerDataBase();

}
