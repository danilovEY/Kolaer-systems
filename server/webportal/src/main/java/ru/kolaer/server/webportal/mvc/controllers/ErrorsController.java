package ru.kolaer.server.webportal.mvc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.entities.other.ExceptionMessageRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by danilovey on 10.11.2016.
 */
@RestController
@RequestMapping(value = "/non-security/errors")
public class ErrorsController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorsController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404")
    public ExceptionMessageRequest notFound(HttpServletRequest request) {
        final String origialUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        final ExceptionMessageRequest exep = new ExceptionMessageRequest("Страница '" + origialUri + "' не найдена!",
                "404", new Date());
        logger.error("Error: {}", exep);
        return exep;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping(value = "/401")
    public ExceptionMessageRequest unAuthorized() {
        final ExceptionMessageRequest exep = new ExceptionMessageRequest("Вы не авторизовались!",
                "401", new Date());
        logger.error("Error: {}", exep);
        return exep;
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @RequestMapping(value = "/503")
    @ExceptionHandler
    private ExceptionMessageRequest exception(HttpServletRequest request, Exception e) throws JsonProcessingException {
        final String origialUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        logger.error("Error on controller: {}", origialUri, (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));

        return new ExceptionMessageRequest(Arrays.toString(e.getStackTrace()),
                "503", new Date());
    }

}
