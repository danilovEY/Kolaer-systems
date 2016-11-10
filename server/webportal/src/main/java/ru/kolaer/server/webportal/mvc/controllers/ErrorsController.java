package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.ExceptionMessageRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
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
        final ExceptionMessageRequest exep = new ExceptionMessageRequest("Page '" + origialUri + "' not found!",
                "404", new Date());
        logger.error("Error: {}", exep);
        return exep;
    }

}
