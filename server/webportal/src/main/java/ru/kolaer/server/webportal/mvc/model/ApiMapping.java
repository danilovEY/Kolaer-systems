package ru.kolaer.server.webportal.mvc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danilovey on 26.07.2016.
 */
public class ApiMapping {
    private Map<String, String> linkMapping;

    public ApiMapping() {
        this.linkMapping = new HashMap<>();
    }

    public void put(String link, String desc) {
        this.linkMapping.put(link, desc);
    }

    public Map<String, String> getLinkMapped() {
        return this.linkMapping;
    }

    public void setLinkMapping(Map<String, String> linkMapping) {
        this.linkMapping = linkMapping;
    }
}
