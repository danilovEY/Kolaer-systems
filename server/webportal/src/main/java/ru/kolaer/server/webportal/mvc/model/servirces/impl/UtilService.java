package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class UtilService {

    @Value("${host.current.url}")
    private String currentHostUrl;

    @Value("${path.external.photo}")
    private String externalPhotoPath;

}
