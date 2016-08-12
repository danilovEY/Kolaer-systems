package ru.kolaer.server.webportal.mvc.controllers.jsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

/**
 * Created by danilovey on 26.07.2016.
 * JSP-контроллер. Позволяет получить страницу с таблицов из url|описание|разрешения для ролей.
 */
@Controller
@RequestMapping("/api")
public class ApiMapController {

    @Autowired
    private UrlPathService urlPathService;

    @UrlDeclaration(url = "/api/mapping", description = "Получить все ссылки.", isAccessAll = true)
    @RequestMapping(value = "/mapping", method = RequestMethod.GET)
    public ModelAndView getMapControllers() {
        final ModelAndView view = new ModelAndView("api-mapping");
        view.addObject("listApi", urlPathService.getAll());
        return view;
    }

}
