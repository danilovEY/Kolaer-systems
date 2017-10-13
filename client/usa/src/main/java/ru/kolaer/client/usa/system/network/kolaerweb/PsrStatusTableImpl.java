package ru.kolaer.client.usa.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.system.network.kolaerweb.PsrStatusTable;

/**
 * Created by danilovey on 23.08.2016.
 */
public class PsrStatusTableImpl implements PsrStatusTable {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_GET_ALL;

    public PsrStatusTableImpl(String path) {
        this.URL_GET_ALL = path + "/get/all";
    }

    @Override
    public PsrStatus[] getAllPsrStatus() {
        return restTemplate.getForObject(this.URL_GET_ALL, PsrStatus[].class);
    }
}
