package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.other.WeatherKaesDto;

/**
 * Created by danilovey on 15.02.2018.
 */
public interface KaesTable {
    ServerResponse<WeatherKaesDto> getWeather();

    String getWeatherChartUrl();
}
