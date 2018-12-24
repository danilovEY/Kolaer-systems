package ru.kolaer.common.system.network;

import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.other.WeatherKaesDto;

/**
 * Created by danilovey on 15.02.2018.
 */
public interface KaesTable {
    ServerResponse<WeatherKaesDto> getWeather();

    String getWeatherChartUrl();
}
