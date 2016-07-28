package ru.kolaer.server.webportal.config;

/**
 * Created by danilovey on 26.07.2016.
 */
public interface PathMapping {
    /**Путь до диспатчера сервлета.*/
    String DISPATCHER_SERVLET = "/rest";

    String PATH_TO_RSS = "/rss";
    String PATH_TO_GET_RSS = "/get";
    String PATH_TO_GET_ALL_RSS = "/all";
    String PARAM_TO_GET_RSS_BY_ID = "id";

    String ABSOLUTE_PATH_PATH_TO_GET_RSS = DISPATCHER_SERVLET + PATH_TO_RSS + PATH_TO_GET_RSS;
    String ABSOLUTE_PATH_PARAM_TO_GET_RSS_BY_ID = ABSOLUTE_PATH_PATH_TO_GET_RSS + "?" + PARAM_TO_GET_RSS_BY_ID + "={id}";
    String ABSOLUTE_PATH_TO_GET_ALL_RSS = ABSOLUTE_PATH_PATH_TO_GET_RSS + PATH_TO_GET_ALL_RSS;
}
