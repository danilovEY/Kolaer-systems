package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Created by danilovey on 14.03.2017.
 */
@Data
@JsonIgnoreProperties(value = {"Correlation", "Security"})
public class System {
    @JacksonXmlProperty(localName = "Provider")
    private Provider provider;
    @JacksonXmlProperty(localName = "EventID")
    private Integer eventID;
    @JacksonXmlProperty(localName = "Version")
    private Integer version;
    @JacksonXmlProperty(localName = "Level")
    private Integer level;
    @JacksonXmlProperty(localName = "Task")
    private Integer task;
    @JacksonXmlProperty(localName = "Opcode")
    private Integer opcode;
    @JacksonXmlProperty(localName = "Keywords")
    private String keyword;
    @JacksonXmlProperty(localName = "TimeCreated")
    private TimeCreated timeCreated;
    @JacksonXmlProperty(localName = "EventRecordID")
    private Integer eventRecordId;
    @JacksonXmlElementWrapper(localName = "Execution")
    private Execution execution;
    @JacksonXmlProperty(localName = "Channel")
    private String channel;
    @JacksonXmlProperty(localName = "Computer")
    private String computer;
}
