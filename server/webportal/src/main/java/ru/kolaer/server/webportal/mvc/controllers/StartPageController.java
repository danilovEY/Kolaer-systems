package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by danilovey on 15.07.2016.
 */
@Controller
public class StartPageController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getIndexPage(Model model) {
        model.addAttribute("welcomeMassage", "Привет!");
        return "login";
    }
}
