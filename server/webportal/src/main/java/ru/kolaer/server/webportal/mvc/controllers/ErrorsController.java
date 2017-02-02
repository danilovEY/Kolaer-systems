package ru.kolaer.server.webportal.mvc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.dto.ExceptionMessageRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by danilovey on 10.11.2016.
 */
@RestController
@RequestMapping(value = "/non-security/errors")
@Api(tags = "Ошибки", description = "Контроллер с ошибками")
public class ErrorsController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorsController.class);

    @ApiOperation(
            value = "404",
            notes = "Страница не найдена"
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404")
    public ExceptionMessageRequest notFound(HttpServletRequest request) {
        final String origialUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        final ExceptionMessageRequest exep = new ExceptionMessageRequest("Страница '" + origialUri + "' не найдена!",
                "404", new Date());
        logger.error("Error: {}", exep);
        return exep;
    }

    @ApiOperation(
            value = "401",
            notes = "Не авторизовался"
    )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping(value = "/401")
    public ExceptionMessageRequest unAuthorized() {
        final ExceptionMessageRequest exep = new ExceptionMessageRequest("Вы не авторизовались!",
                "401", new Date());
        logger.error("Error: {}", exep);
        return exep;
    }

    @ApiOperation(
            value = "503",
            notes = "Ошибка на сервере"
    )
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @RequestMapping(value = "/503")
    @ExceptionHandler
    private ExceptionMessageRequest exception(HttpServletRequest request) throws JsonProcessingException {
        final String origialUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        final Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        logger.error("Error on controller: {}", origialUri, exception);

        return new ExceptionMessageRequest(exception.getCause().getMessage(),
                "503", new Date());
    }

    @ApiOperation(
            value = "403",
            notes = "Нет доступа"
    )
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(value = "/403")
    private ExceptionMessageRequest forbidden(HttpServletRequest request) throws JsonProcessingException {
        final String origialUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        final String message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
        logger.error("Error on controller: {}", origialUri);
        logger.error("Error message: {}", message);

        return new ExceptionMessageRequest(message,
                "403", new Date());
    }

}
