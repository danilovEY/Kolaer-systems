package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.entities.other.ExceptionMessageRequest;

import java.util.Date;

/**
 * Created by danilovey on 11.01.2017.
 */
@Slf4j
public abstract class BaseController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionMessageRequest handleException(BadRequestException e) {
        log.warn(e.getMessage());
        return new ExceptionMessageRequest(e.getMessage(),
                "400", new Date());
    }

}
