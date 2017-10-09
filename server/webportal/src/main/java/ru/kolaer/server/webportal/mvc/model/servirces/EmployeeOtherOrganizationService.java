package ru.kolaer.server.webportal.mvc.model.servirces;

import org.xml.sax.SAXException;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by danilovey on 03.11.2016.
 */
public interface EmployeeOtherOrganizationService extends
        BirthdayService<EmployeeOtherOrganizationDto>,
        DefaultService<EmployeeOtherOrganizationDto> {
    void updateFromXml(InputStream file) throws ParserConfigurationException, IOException, SAXException;
}
