package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created by danilovey on 15.03.2017.
 */
@Data
public class Event {
    @JacksonXmlProperty(localName = "System")
    private System system;

    @JacksonXmlProperty(localName = "EventData")
    private EventData eventData;
}
