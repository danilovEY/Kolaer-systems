package ru.kolaer.client.javafx.system.network.kolaerweb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
class KolpassTableImpl implements KolpassTable, TokenToHeader {
    private ObjectMapper mapper;
    private final String PATH;
    private final String GET_ALL_MY_REPOSITORY_PASS;
    private final String ADD_HISTORY_PASSWORD;

    KolpassTableImpl(String path) {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.PATH = path;
        this.GET_ALL_MY_REPOSITORY_PASS = this.PATH + "/get/all/personal";
        this.ADD_HISTORY_PASSWORD = this.PATH + "/add";
    }

    @Override
    public List<RepositoryPassword> getAllRepositoryPasswords() {
        final String body = restTemplate
                .exchange(this.GET_ALL_MY_REPOSITORY_PASS,
                        HttpMethod.GET,
                        new HttpEntity<>(this.getTokenToHeader()),
                        String.class)
                .getBody();
        try {
            return ((Page<RepositoryPassword>) mapper
                    .readValue(body, new TypeReference<Page<RepositoryPassword>>(){})).getData();
        } catch (IOException e) {
            log.error("Ошибка при чтении!", e);
        }

        return Collections.emptyList();
    }

    @Override
    public RepositoryPassword addRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        return restTemplate.exchange(this.ADD_HISTORY_PASSWORD,
                HttpMethod.POST,
                new HttpEntity<>(repositoryPasswordDto, this.getTokenToHeader()),
                RepositoryPassword.class).getBody();
    }

    @Override
    public RepositoryPassword updateRepositoryPassword(RepositoryPassword repositoryPasswordDto) {
        return null;
    }

    @Override
    public void deleteRepositoryPassword(RepositoryPassword repositoryPasswordDto) {

    }
}
