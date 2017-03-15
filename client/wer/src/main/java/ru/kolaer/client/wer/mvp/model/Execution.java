package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created by danilovey on 15.03.2017.
 */
@Data
public class Execution {
    @JacksonXmlProperty(localName = "ProcessID", isAttribute = true)
    private Integer processId;
    @JacksonXmlProperty(localName = "ThreadID", isAttribute = true)
    private Integer threadId;
}
