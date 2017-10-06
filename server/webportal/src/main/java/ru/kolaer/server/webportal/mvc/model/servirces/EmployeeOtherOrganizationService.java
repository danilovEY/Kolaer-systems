package ru.kolaer.server.webportal.mvc.model.servirces;

import org.xml.sax.SAXException;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 03.11.2016.
 */
public interface EmployeeOtherOrganizationService extends ServiceBase<EmployeeOtherOrganizationDto> {
    void updateFromXml(InputStream file) throws ParserConfigurationException, IOException, SAXException;
    List<EmployeeOtherOrganizationDto> getUserRangeBirthday(Date startData, Date endData);
    List<EmployeeOtherOrganizationDto> getUsersByBirthday(Date date);
    List<EmployeeOtherOrganizationDto> getUserBirthdayToday();
    List<EmployeeOtherOrganizationDto> getUsersByInitials(String initials);
    int getCountUserBirthday(Date date);

}
