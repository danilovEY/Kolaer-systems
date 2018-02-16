package ru.kolaer.api.mvp.model.other;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by danilovey on 15.02.2018.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherKaesDto {

    @JsonProperty("data")
    private WeatherKaesDataDto data;

    @JsonProperty("dbupd")
    @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, timezone = "Europe/Moscow", pattern = "dd.MM.yyyy HH:mm")
    private Date updateTime;
}
