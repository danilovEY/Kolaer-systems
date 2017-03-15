package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * Created by danilovey on 15.03.2017.
 */
@JacksonXmlRootElement(localName = "Events")
@Data
public class XmlWindowsEvents {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Event")
    private Event[] events;
}
