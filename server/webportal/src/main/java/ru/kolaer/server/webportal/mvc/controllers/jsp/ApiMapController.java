package ru.kolaer.server.webportal.mvc.controllers.jsp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

/**
 * Created by danilovey on 26.07.2016.
 * JSP-контроллер. Позволяет получить страницу с таблицов из url|описание|разрешения для ролей.
 */
@Controller
@RequestMapping("/api")
@Api(value = "API", tags = "JSP")
public class ApiMapController {
    private final UrlSecurityService urlSecurityService;

    @Autowired
    public ApiMapController(UrlSecurityService urlSecurityService) {
        this.urlSecurityService = urlSecurityService;
    }

    @UrlDeclaration(description = "Получить все ссылки.", isAccessAll = true)
    @RequestMapping(value = "/mapping", method = RequestMethod.GET)
    @ApiOperation("Получить страницу")
    public ModelAndView getMapControllers() {
        final ModelAndView view = new ModelAndView("api-mapping");
        view.addObject("listApi", urlSecurityService.getAll());
        return view;
    }

}
