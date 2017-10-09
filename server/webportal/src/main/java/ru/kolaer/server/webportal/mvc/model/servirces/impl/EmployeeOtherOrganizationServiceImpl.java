package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.webportal.mvc.model.converter.EmployeeOtherOrganizationConverter;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeOtherOrganizationDao;
import ru.kolaer.server.webportal.mvc.model.entities.birthday.EmployeeOtherOrganizationEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeOtherOrganizationService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 03.11.2016.
 */
@Service
@Slf4j
public class EmployeeOtherOrganizationServiceImpl
        extends AbstractDefaultService<EmployeeOtherOrganizationDto, EmployeeOtherOrganizationEntity>
        implements EmployeeOtherOrganizationService {

    private final EmployeeOtherOrganizationDao employeeOtherOrganizationDao;

    protected EmployeeOtherOrganizationServiceImpl(EmployeeOtherOrganizationDao employeeOtherOrganizationDao,
                                                   EmployeeOtherOrganizationConverter converter) {
        super(employeeOtherOrganizationDao, converter);
        this.employeeOtherOrganizationDao = employeeOtherOrganizationDao;
    }


    @Override
    public void updateFromXml(InputStream file) throws ParserConfigurationException, IOException, SAXException {
         if(file == null)
             throw new IllegalArgumentException("File is null!");

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        final Document parse = documentBuilder.parse(file);

        NodeList elementsByTagName = parse.getElementsByTagName("z:row");

        final List<EmployeeOtherOrganizationEntity> result = new ArrayList<>();

        for(int i = 0; i < elementsByTagName.getLength(); i++) {
            final Node item = elementsByTagName.item(i);
            if(item.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) item;
                final String organization = element.getAttribute("ows_Company");
                if(!organization.equals("КолАтомэнергоремонт")) {
                    final String name = element.getAttribute("ows_LinkTitleNoMenu");
                    final String post = element.getAttribute("ows_JobTitle");
                    final String department = element.getAttribute("ows_Department");
                    final String mobilePhone = element.getAttribute("ows_PrimaryNumber");
                    final String phone = element.getAttribute("ows_InternalPhone");
                    final String email = element.getAttribute("ows_EMail");
                    final String birthday = element.getAttribute("ows_Birthday");

                    final EmployeeOtherOrganizationEntity employee = new EmployeeOtherOrganizationEntity();
                    employee.setInitials(name);
                    employee.setPost(post);
                    employee.setDepartment(department);
                    employee.setWorkPhoneNumber(Optional.ofNullable(mobilePhone).map(mPhone -> mPhone + "; " + phone).orElse(phone));
                    employee.setEmail(email);
                    employee.setOrganization(organization);

                    String postToLower = post.toLowerCase();

                    if(postToLower.contains("руководитель") || postToLower.contains("начальник")
                            || postToLower.contains("заместитель") || post.contains("главный")
                            || post.contains("директора")) {
                        employee.setCategoryUnit("Руководитель");
                    } else {
                        employee.setCategoryUnit("Специалист");
                    }

                    try {
                        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if(birthday != null && !birthday.trim().isEmpty()) {
                            employee.setBirthday(df.parse(birthday));
                        }
                    } catch (ParseException e) {
                        log.error("Невозможно преобразовать строку: {} в дату!", birthday);
                    }

                    result.add(employee);
                }
            }
        }
        log.info("Employees size: {}", result.size());
        this.employeeOtherOrganizationDao.update(result);
    }

    @Override
    public List<EmployeeOtherOrganizationDto> getUserRangeBirthday(Date startData, Date endData) {
        return employeeOtherOrganizationDao.getUserRangeBirthday(startData, endData)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeOtherOrganizationDto> getUsersByBirthday(Date date) {
        return employeeOtherOrganizationDao.getUsersByBirthday(date)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeOtherOrganizationDto> getUserBirthdayToday() {
        return employeeOtherOrganizationDao.getUserBirthdayToday()
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeOtherOrganizationDto> getUsersByInitials(String initials) {
        return employeeOtherOrganizationDao.getUsersByInitials(initials)
                .stream()
                .map(baseConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getCountUserBirthday(Date date) {
        return employeeOtherOrganizationDao.getCountUserBirthday(date);
    }

}
