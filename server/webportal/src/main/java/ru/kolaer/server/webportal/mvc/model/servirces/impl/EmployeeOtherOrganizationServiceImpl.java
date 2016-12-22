package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationBase;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeOtherOrganizationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeOtherOrganizationService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 03.11.2016.
 */
@Service
public class EmployeeOtherOrganizationServiceImpl implements EmployeeOtherOrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeOtherOrganizationServiceImpl.class);

    @Autowired
    private EmployeeOtherOrganizationDao employeeOtherOrganizationDao;


    @Override
    public void updateFromXml(InputStream file) throws ParserConfigurationException, IOException, SAXException {
         if(file == null)
             throw new IllegalArgumentException("File is null!");

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        final Document parse = documentBuilder.parse(file);

        NodeList elementsByTagName = parse.getElementsByTagName("z:row");

        final List<EmployeeOtherOrganization> result = new ArrayList<>();

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

                    final EmployeeOtherOrganization employee = new EmployeeOtherOrganizationBase();
                    employee.setInitials(name);
                    employee.setPost(post);
                    employee.setDepartament(department);
                    employee.setMobilePhone(mobilePhone);
                    employee.setPhone(phone);
                    employee.setEmail(email);
                    employee.setOrganization(organization);

                    if(post.contains("Руководитель") || post.contains("Начальник")
                            || post.contains("Заместитель") || post.contains("заместитель")
                            || post.contains("Главный") || post.contains("Директор")
                            || post.contains("директора")) {
                        employee.setCategoryUnit("Руководитель");
                    } else {
                        employee.setCategoryUnit("Специалист");
                    }

                    try {
                        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if(birthday == null || birthday.trim().isEmpty())
                            employee.setBirthday(new Calendar.Builder().setDate(9999,1,1).build().getTime());
                        else
                            employee.setBirthday(df.parse(birthday));
                    } catch (ParseException e) {
                        logger.error("Невозможно преобразовать строку: {} в дату!", birthday);
                    }

                    result.add(employee);
                }
            }
        }
        logger.info("Employees size: {}", result.size());
        this.employeeOtherOrganizationDao.update(result);
    }

    @Override
    public List<EmployeeOtherOrganization> getUserRangeBirthday(Date startData, Date endData) {
        return employeeOtherOrganizationDao.getUserRangeBirthday(startData, endData);
    }

    @Override
    public List<EmployeeOtherOrganization> getUsersByBirthday(Date date) {
        return employeeOtherOrganizationDao.getUsersByBirthday(date);
    }

    @Override
    public List<EmployeeOtherOrganization> getUserBirthdayToday() {
        return employeeOtherOrganizationDao.getUserBirthdayToday();
    }

    @Override
    public List<EmployeeOtherOrganization> getUsersByInitials(String initials) {
        return employeeOtherOrganizationDao.getUsersByInitials(initials);
    }

    @Override
    public int getCountUserBirthday(Date date) {
        return employeeOtherOrganizationDao.getCountUserBirthday(date);
    }

    @Override
    public List<EmployeeOtherOrganization> getAll() {
        return employeeOtherOrganizationDao.getAll();
    }

    @Override
    public EmployeeOtherOrganization getById(Integer id) {
        return null;
    }

    @Override
    public void add(EmployeeOtherOrganization entity) {
        employeeOtherOrganizationDao.insertData(entity);
    }

    @Override
    public void delete(EmployeeOtherOrganization entity) {

    }

    @Override
    public void update(EmployeeOtherOrganization entity) {

    }

    @Override
    public void update(List<EmployeeOtherOrganization> entity) {
        if(entity.size() > 0) {
            this.employeeOtherOrganizationDao.update(entity);
        }
    }

    @Override
    public void delete(List<EmployeeOtherOrganization> entites) {

    }
}
