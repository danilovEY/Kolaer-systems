package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created by danilovey on 15.03.2017.
 */
@Data
public class Provider {
    @JacksonXmlProperty(localName = "Name", isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "Guid", isAttribute = true)
    private String grid;
}
