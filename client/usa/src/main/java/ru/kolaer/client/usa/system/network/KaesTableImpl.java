package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.other.WeatherKaesDto;
import ru.kolaer.common.system.network.KaesTable;

/**
 * Created by danilovey on 15.02.2018.
 */
public class KaesTableImpl implements KaesTable, RestTemplateService {
    private final ObjectMapper objectMapper;
    private final RestTemplate globalRestTemplate;
    private final String url;

    private final String URL_GET_WEATHER;
    private final String URL_GET_WEATHER_CHART;

    public KaesTableImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String url) {
        this.objectMapper = objectMapper;
        this.globalRestTemplate = globalRestTemplate;
        this.url = url;

        URL_GET_WEATHER = url + "/runstr/api/json";
        URL_GET_WEATHER_CHART = url + "/store/weather/informer_graf-640.gif";
    }

    @Override
    public ServerResponse<WeatherKaesDto> getWeather() {
        return getServerResponse(globalRestTemplate, URL_GET_WEATHER, WeatherKaesDto.class, objectMapper);
    }

    @Override
    public String getWeatherChartUrl() {
        return URL_GET_WEATHER_CHART;
    }
}
