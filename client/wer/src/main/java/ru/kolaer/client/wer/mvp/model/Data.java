package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * Created by danilovey on 15.03.2017.
 */
@lombok.Data
public class Data {
    @JacksonXmlProperty(localName = "Name", isAttribute = true)
    private String name;

    @JacksonXmlText
    private String value;
}
