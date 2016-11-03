package ru.kolaer.server.webportal.mvc.model.servirces;

import org.xml.sax.SAXException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 03.11.2016.
 */
public interface EmployeeOtherOrganizationService extends ServiceBase<EmployeeOtherOrganization> {
    void updateFromXml(InputStream file) throws ParserConfigurationException, IOException, SAXException;
    List<EmployeeOtherOrganization> getUserRangeBirthday(Date startData, Date endData);
    List<EmployeeOtherOrganization> getUsersByBirthday(Date date);
    List<EmployeeOtherOrganization> getUserBirthdayToday();
    List<EmployeeOtherOrganization> getUsersByInitials(String initials);
    int getCountUserBirthday(Date date);

}
