package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.system.network.kolaerweb.CounterTable;

/**
 * Created by danilovey on 25.08.2016.
 */
public class CounterTableImpl implements CounterTable {
    private final RestTemplate restTemplate;
    private final String URL_GET_ALL;

    public CounterTableImpl(String path) {
        this.restTemplate = new RestTemplate();
        this.URL_GET_ALL = path + "/get/all";
    }

    @Override
    public Counter[] getAllCounters() {
        return this.restTemplate.getForObject(this.URL_GET_ALL, Counter[].class);
    }

    @Override
    public void addCounter(Counter counter) {

    }

    @Override
    public void updateCounter(Counter counter) {

    }

    @Override
    public void deleteCounter(Counter counter) {

    }
}
