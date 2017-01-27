package ru.kolaer.server.webportal.mvc.controllers.jsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeOtherOrganizationService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdateEmployeesService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by danilovey on 03.11.2016.
 */
@Controller
@RequestMapping(value = "upload")
public class UploadController {

    @Autowired
    private EmployeeOtherOrganizationService employeeOtherOrganizationService;

    @Autowired
    private UpdateEmployeesService employeeService;

    @RequestMapping(method = RequestMethod.GET)
    public String getStartPage() {
        return "tools";
    }

    @RequestMapping(value = "/employees/other", method = RequestMethod.POST)
    public String uploadEmployeeOtherOrganization(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes)
            throws IOException, ParserConfigurationException, SAXException {
        this.employeeOtherOrganizationService.updateFromXml(file.getInputStream());
        redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");
        return "tools";
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public String uploadEmployee(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes)
            throws IOException, ParserConfigurationException, SAXException {
        this.employeeService.updateEployees(file.getInputStream());
        redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");
        return "tools";
    }

}
