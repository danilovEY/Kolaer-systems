package ru.kolaer.server.counter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.server.counter.service.CounterService;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@RequestMapping("/non-security/counters")
@Api(tags = "Счетчик")
public class CounterControllers {

    private final CounterService counterService;

    @Autowired
    public CounterControllers(CounterService counterService) {
        this.counterService = counterService;
    }

    @ApiOperation("Получить все счетчики")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CounterDto> getAllCounters() {
        return  this.counterService.getAll();
    }

}
