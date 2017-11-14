package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.client.usa.system.network.kolaerweb.TokenToHeader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 16.10.2017.
 */
public interface RestTemplateService extends TokenToHeader {

    default <T> ServerResponse<Page<T>> getPageResponse(RestTemplate restTemplate, String url, ObjectMapper objectMapper) {
        try {
            return getPageResponse(restTemplate.exchange(url, HttpMethod.GET, getHeader(), String.class), objectMapper);
        } catch (RestClientException ex) {
            return createServerExceptionMessage(url);
        }
    }

    default <T> ServerResponse<T> getServerResponse(RestTemplate restTemplate, String url, Class<T> dtoClass, ObjectMapper objectMapper) {
        try {
            return getServerResponse(restTemplate.exchange(url, HttpMethod.GET, getHeader(), String.class), dtoClass, objectMapper);
        } catch (RestClientException ex) {
            return createServerExceptionMessage(url);
        }
    }

    default <T> ServerResponse<List<T>> getServerResponses(RestTemplate restTemplate, String url, Class<T[]> dtoClass, ObjectMapper objectMapper) {
        try {
            return getServerResponses(restTemplate.exchange(url, HttpMethod.GET, getHeader(), String.class), dtoClass, objectMapper);
        } catch (RestClientException ex) {
            return createServerExceptionMessage(url);
        }
    }

    default <T> ServerResponse<T> postServerResponse(RestTemplate restTemplate, String url, Object request, Class<T> dtoClass, ObjectMapper objectMapper) {
        try {
            return getServerResponse(restTemplate.exchange(url, HttpMethod.POST, getHeader(request), String.class), dtoClass, objectMapper);
        } catch (RestClientException ex) {
            return createServerExceptionMessage(url);
        }
    }

    default <T> ServerResponse<List<T>> postServerResponses(RestTemplate restTemplate, String url, Object request, Class<T[]> dtoClass, ObjectMapper objectMapper) {
        try {
            return getServerResponses(restTemplate.exchange(url, HttpMethod.POST, getHeader(request), String.class), dtoClass, objectMapper);
        } catch (RestClientException ex) {
            return createServerExceptionMessage(url);
        }
    }

    default <T> ServerResponse<T> getServerResponse(ResponseEntity<String> response, Class<T> dtoClass, ObjectMapper objectMapper) {
        ServerResponse<T> serverResponse = new ServerResponse<>();
        try {
            HttpStatus statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                if(dtoClass != null) {
                    serverResponse.setResponse(readValue(objectMapper, response.getBody(), dtoClass));
                }
            } else {
                serverResponse.setServerError(true);
                serverResponse.setExceptionMessage(readException(objectMapper, response.getBody()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать ответ с сервера", e);
        }

        return serverResponse;
    }

    default <T> ServerResponse<List<T>> getServerResponses(ResponseEntity<String> response, Class<T[]> dtoClass, ObjectMapper objectMapper) {
        ServerResponse<List<T>> serverResponse = new ServerResponse<>();
        try {
            HttpStatus statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                if(dtoClass != null) {
                    serverResponse.setResponse(readValues(objectMapper, response.getBody(), dtoClass));
                }
            } else {
                serverResponse.setResponse(Collections.emptyList());
                serverResponse.setServerError(true);
                serverResponse.setExceptionMessage(readException(objectMapper, response.getBody()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать ответ с сервера", e);
        }

        return serverResponse;
    }

    default <T> ServerResponse<Page<T>> getPageResponse(ResponseEntity<String> response, ObjectMapper objectMapper) {
        ServerResponse<Page<T>> serverResponse = new ServerResponse<>();
        try {
            HttpStatus statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                serverResponse.setResponse(readPageValue(objectMapper, response.getBody()));
            } else {
                serverResponse.setServerError(true);
                serverResponse.setExceptionMessage(readException(objectMapper, response.getBody()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать ответ с сервера", e);
        }

        return serverResponse;
    }

    default <T> List<T> readValues(ObjectMapper objectMapper, String content, Class<T[]> dtoClass) throws IOException {
        return StringUtils.hasText(content)
                ?  Arrays.asList(objectMapper.readValue(content, dtoClass))
                : Collections.emptyList();
    }

    default <T> T readValue(ObjectMapper objectMapper, String content, Class<T> dtoClass) throws IOException {
        return StringUtils.hasText(content)
                ? objectMapper.readValue(content, dtoClass)
                : null;
    }

    default <T> Page<T> readPageValue(ObjectMapper objectMapper, String content) throws IOException {
        return StringUtils.hasText(content)
                ? objectMapper.readValue(content, new TypeReference<Page<T>>() {})
                : Page.createPage();
    }

    default ServerExceptionMessage readException(ObjectMapper objectMapper, String content) throws IOException {
        return StringUtils.hasText(content)
                ? objectMapper.readValue(content, ServerExceptionMessage.class)
                : new ServerExceptionMessage();
    }

    default <T> ServerResponse<T> createServerExceptionMessage(String url) {
        return new ServerResponse<>(true, new ServerExceptionMessage(0, url, ErrorCode.CONNECT), null);
    }
}
