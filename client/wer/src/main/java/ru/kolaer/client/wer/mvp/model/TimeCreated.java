package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by danilovey on 15.03.2017.
 */
@Data
public class TimeCreated {
    @JacksonXmlProperty(localName = "SystemTime", isAttribute = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "+3")
    private Date systemTime;
}
