package ru.kolaer.client.usa.system.network.kolaerweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.system.network.kolaerweb.ServerTools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by danilovey on 25.08.2016.
 */
public class ServerToolsImpl implements ServerTools {
    private static final Logger LOG = LoggerFactory.getLogger(ServerToolsImpl.class);
    private final RestTemplate restTemplate;
    private final String URL_GET_TIME;

    public ServerToolsImpl(RestTemplate globalRestTemplate, String path) {
        this.restTemplate = globalRestTemplate;
        this.URL_GET_TIME = path + "/non-security/tools/get/time";
    }

    @Override
    public LocalDateTime getCurrentDataTime() {
        try {
            final DateTimeJson dateTimeJson = this.restTemplate.getForObject(this.URL_GET_TIME, DateTimeJson.class);
            return LocalDateTime.parse(dateTimeJson.toString(),  DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm:ss"));
        } catch (Exception e) {
            LOG.error("Ошибка при получении времени!", e);
            return LocalDateTime.now();
        }

    }
}
