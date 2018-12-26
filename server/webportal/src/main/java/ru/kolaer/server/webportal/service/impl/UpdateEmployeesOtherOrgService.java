package ru.kolaer.server.webportal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.common.dto.kolaerweb.EnumCategory;
import ru.kolaer.common.dto.kolaerweb.EnumGender;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.otheremployee.dao.EmployeeOtherOrganizationDao;
import ru.kolaer.server.otheremployee.model.entity.EmployeeOtherOrganizationEntity;
import ru.kolaer.server.webportal.model.dto.holiday.HistoryChangeDto;
import ru.kolaer.server.webportal.service.UpdateEmployeesService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 13.10.2017.
 */
@Service
@Slf4j
public class UpdateEmployeesOtherOrgService implements UpdateEmployeesService {
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final EmployeeOtherOrganizationDao employeeOtherOrganizationDao;

    public UpdateEmployeesOtherOrgService(EmployeeOtherOrganizationDao employeeOtherOrganizationDao) {
        this.employeeOtherOrganizationDao = employeeOtherOrganizationDao;
    }

    @Override
    @Transactional
    public List<HistoryChangeDto> updateEmployees(File file) {
        try {
            return updateEmployees(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new UnexpectedRequestParams("Не удалось прочитать файл", e, ErrorCode.PARSE_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public List<HistoryChangeDto> updateEmployees(InputStream inputStream) {
        if(inputStream == null)
            throw new IllegalArgumentException("File is null!");

        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document parse = documentBuilder.parse(inputStream);

            NodeList elementsByTagName = parse.getElementsByTagName("z:row");

            List<EmployeeOtherOrganizationEntity> result = new ArrayList<>();

            for(int i = 0; i < elementsByTagName.getLength(); i++) {
                Node item = elementsByTagName.item(i);
                if(item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    String organization = element.getAttribute("ows_Company");
                    if(!organization.equals("КолАтомэнергоремонт")) {
                        String name = element.getAttribute("ows_LinkTitleNoMenu");
                        String post = element.getAttribute("ows_JobTitle");
                        String department = element.getAttribute("ows_Department");
                        String mobilePhone = element.getAttribute("ows_PrimaryNumber");
                        String phone = element.getAttribute("ows_InternalPhone");
                        String email = element.getAttribute("ows_EMail");
                        String birthday = element.getAttribute("ows_Birthday");

                        EmployeeOtherOrganizationEntity employee = new EmployeeOtherOrganizationEntity();
                        employee.setInitials(name);
                        employee.setPost(post);
                        employee.setDepartment(department);
                        employee.setWorkPhoneNumber(Optional.ofNullable(mobilePhone).map(mPhone -> mPhone + "; " + phone).orElse(phone));
                        employee.setEmail(email);
                        employee.setOrganization(organization);

                        if(name.charAt(name.length() - 1) == 'а') {
                            employee.setGender(EnumGender.FEMALE);
                        } else {
                            employee.setGender(EnumGender.MALE);
                        }

                        String[] names = name.split(" ");
                        if(names.length > 1) {
                            employee.setSecondName(names[1]);
                        }
                        if(names.length > 0) {
                            employee.setFirstName(names[0]);
                        }
                        if(names.length > 2) {
                            employee.setThirdName(names[2]);
                        }

                        String postToLower = post.toLowerCase();

                        if(postToLower.contains("руководитель") || postToLower.contains("начальник")
                                || postToLower.contains("заместитель") || postToLower.contains("главный")
                                || postToLower.contains("директора")) {
                            employee.setCategory(EnumCategory.LEADER);
                        } else {
                            employee.setCategory(EnumCategory.SPECIALIST);
                        }

                        try {
                            if(StringUtils.hasText(birthday)) {
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
            employeeOtherOrganizationDao.clear();
            employeeOtherOrganizationDao.persist(result);
        } catch (Exception ex) {
            throw new UnexpectedRequestParams("Не удалось прочитать файл", ex, ErrorCode.PARSE_EXCEPTION);
        }

        return Collections.emptyList();
    }
}
