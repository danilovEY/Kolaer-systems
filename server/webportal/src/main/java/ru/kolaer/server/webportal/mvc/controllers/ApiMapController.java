package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.kolaer.server.webportal.mvc.model.ApiMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danilovey on 26.07.2016.
 */
@Controller
@RequestMapping("/api")
public class ApiMapController {

    @Autowired
    @Qualifier("apiMapping")
    private ApiMapping apiMapping;

    @RequestMapping(value = "/mapping", method = RequestMethod.GET)
    public ModelAndView getMapControllers() {
        final ModelAndView view = new ModelAndView("api-mapping");
        apiMapping.getLinkMapped().forEach((key, value) -> {
            System.out.println(key + " | " + value);
        });
        view.addObject("map", apiMapping.getLinkMapped());
        return view;
    }

}
