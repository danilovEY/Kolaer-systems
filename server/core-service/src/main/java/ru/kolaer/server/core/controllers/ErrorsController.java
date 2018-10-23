package ru.kolaer.server.core.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kolaer.common.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.server.webportal.common.servirces.ExceptionHandlerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by danilovey on 10.11.2016.
 */
@ControllerAdvice
@RequestMapping(value = "/non-security/errors")
@Api(tags = "Ошибки", description = "Контроллер с ошибками")
@Slf4j
public class ErrorsController /*extends ResponseEntityExceptionHandler*/ {
    private ExceptionHandlerService exceptionHandlerService;

    public ErrorsController(ExceptionHandlerService exceptionHandlerService) {
        this.exceptionHandlerService = exceptionHandlerService;
    }

    /**Перехват всех ошибко на сервере.*/
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ServerExceptionMessage defaultExceptionHandler(HttpServletRequest hRequest,
                                                                        Exception exception) {
        return exceptionHandlerService.defaultExceptionHandler(hRequest, exception);
    }

    /**Перехват {@link ServerException} на сервере.*/
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ServerException.class)
    public @ResponseBody ServerExceptionMessage serverExceptionHandler(HttpServletRequest hRequest,
                                                                       ServerException exception) {
        return exceptionHandlerService.serverExceptionHandler(hRequest, exception);
    }

    /**Перехват {@link DataIntegrityViolationException} на сервере.*/
    /*@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public @ResponseBody ServerExceptionMessage sqlException(HttpServletRequest hRequest,
                                                             DataIntegrityViolationException exception) {
        return exceptionHandlerService.sqlException(hRequest, exception);
    }*/

    /**Перехват {@link UnexpectedRequestParams}*/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnexpectedRequestParams.class)
    public @ResponseBody ServerExceptionMessage unExpectedParamExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, UnexpectedRequestParams exception) {
        return exceptionHandlerService.unExpectedParamExceptionHandler(hRequest, hResponse, exception);
    }

    /**Перехват {@link NotFoundDataException}*/
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundDataException.class)
    public @ResponseBody ServerExceptionMessage notFoundDataExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, NotFoundDataException exception) {
        return exceptionHandlerService.notFoundDataExceptionHandler(hRequest, hResponse, exception);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value =  BadCredentialsException.class)
    public @ResponseBody ServerExceptionMessage BadCredentialsExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse,  BadCredentialsException exception) {
        return exceptionHandlerService.authExceptionHandler(hRequest, hResponse, new BadCredentialsException("Неправильный логин или пароль"));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value =  AuthenticationException.class)
    public @ResponseBody ServerExceptionMessage authExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse,  AuthenticationException exception) {
        return exceptionHandlerService.authExceptionHandler(hRequest, hResponse, exception);
    }

    /**Перехват {@link ForbiddenException}*/
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = ForbiddenException.class)
    public @ResponseBody ServerExceptionMessage authExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, ForbiddenException exception) {
        return exceptionHandlerService.forbiddenExceptionHandler(hRequest, hResponse, exception);
    }

    /**Перехват {@link CustomHttpCodeException}*/
    @ExceptionHandler(value = CustomHttpCodeException.class)
    public @ResponseBody ServerExceptionMessage authExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, CustomHttpCodeException exception) {
        return exceptionHandlerService.customExceptionHandler(hRequest, hResponse, exception);
    }

    /**403*/
    @ApiOperation(value = "", hidden = true)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(value = "403", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ServerExceptionMessage forbidden(HttpServletRequest hRequest) {
        return exceptionHandlerService.forbidden(hRequest);
    }

    /**401*/
    @ApiOperation(value = "", hidden = true)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping(value = "401", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ServerExceptionMessage authorization(HttpServletRequest hRequest) {
        return exceptionHandlerService.authorization(hRequest);
    }

    /**404*/
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @RequestMapping(value = "404", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ServerExceptionMessage notFound(NoHandlerFoundException ex, HttpServletRequest hRequest) {
        return exceptionHandlerService.notFound(ex, hRequest);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody ServerExceptionMessage notFound(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return exceptionHandlerService.handleHttpRequestMethodNotSupported(ex, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody ServerExceptionMessage notFound(HttpMessageNotReadableException ex, WebRequest request) {
        return exceptionHandlerService.handleHttpMessageNotReadable(ex, request);
    }
}