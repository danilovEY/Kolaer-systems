package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.HolidaysTable;
import ru.kolaer.client.usa.system.network.RestTemplateService;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by danilovey on 02.11.2016.
 */
public class HolidaysTableImpl implements HolidaysTable, RestTemplateService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String URL_GET;
    private final String URL_GET_ALL;

    public HolidaysTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;
        this.URL_GET = path + "/get";
        this.URL_GET_ALL = URL_GET + "/all";
    }

    @Override
    public ServerResponse<List<Holiday>> getHolidaysInThisMonth() {
        LocalDate date = LocalDate.now();
        return this.getHolidays(date.getMonthValue(), date.getYear());
    }

    @Override
    public ServerResponse<List<Holiday>> getHolidays(final int month, final int year) {
        return getServerResponses(restTemplate.getForEntity(URL_GET + "/" + String.valueOf(month) + "/" + String.valueOf(year), String.class),
                Holiday[].class, objectMapper);
    }

    @Override
    public ServerResponse<List<Holiday>> getHolidaysAll() {
        return getServerResponses(restTemplate.getForEntity(URL_GET_ALL, String.class),
                Holiday[].class, objectMapper);
    }
}
