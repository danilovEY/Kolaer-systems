package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.CounterService;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@RequestMapping("/non-security/counters")
public class CounterControllers {

    @Autowired
    private CounterService counterService;

    @UrlDeclaration(description = "Получить все счетчики", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Counter> getAllCounters() {
        return  this.counterService.getAll();
    }

}
