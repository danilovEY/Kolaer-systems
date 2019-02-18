package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.kolaerweb.CounterTable;
import ru.kolaer.client.usa.system.network.RestTemplateService;
import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
public class CounterTableImpl implements CounterTable, RestTemplateService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String URL_GET_ALL;

    public CounterTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.objectMapper = objectMapper;
        this.restTemplate = globalRestTemplate;
        this.URL_GET_ALL = path + "/get/all";
    }

    @Override
    public ServerResponse<List<CounterDto>> getAllCounters() {
        return getServerResponses(restTemplate, URL_GET_ALL, CounterDto[].class, objectMapper);
    }

    @Override
    public ServerResponse addCounter(CounterDto counter) {
        return null;
    }

    @Override
    public ServerResponse updateCounter(CounterDto counter) {
        return null;
    }

    @Override
    public ServerResponse deleteCounter(CounterDto counter) {
        return null;
    }
}
