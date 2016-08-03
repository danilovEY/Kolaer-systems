package ru.kolaer.client.javafx.system.network.kolaerweb;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public class PsrTableImpl implements PsrTable {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_GET_ALL;

    public PsrTableImpl(String path) {
        this.URL_GET_ALL = path + "/get/all";
    }

    @Override
    public PsrRegister[] getAllPsrRegister() throws ServerException {
        try {
            return this.restTemplate.getForObject(this.URL_GET_ALL, PsrRegister[].class);
        } catch (RestClientException ex) {
            throw new ServerException(ex);
        }
    }
}
