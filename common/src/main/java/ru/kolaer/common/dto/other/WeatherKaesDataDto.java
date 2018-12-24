package ru.kolaer.common.dto.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by danilovey on 15.02.2018.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherKaesDataDto {

    @JsonProperty("n_aes")
    private String power;

    @JsonProperty("askro_med")
    private String radiation;

    @JsonProperty("askro_temp")
    private String temperature;

    @JsonProperty("askro_wind")
    private String wind;
}
