package ru.kolaer.server.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kolaer.common.dto.error.ServerExceptionMessage;
import ru.kolaer.server.core.exception.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface ExceptionHandlerService {
    Logger log = LoggerFactory.getLogger(ExceptionHandlerService.class);
    String UNAUTHORIZED_MESSAGE = "Вы не авторизовались!";
    String FORBIDDEN_MESSAGE = "У вас нет доступа!";
    String BAD_REQUEST_MESSAGE = "Не удалось получить объект!";
    int SERVER_EXCEPTION_CODE = 500;
    int METHOD_NOT_ALLOWED = 405;
    int UNAUTHORIZED_CODE = 401;
    int BAD_REQUEST_CODE = 400;
    int NOT_FOUND_CODE = 404;
    int FORBIDDEN_CODE = 403;

    /**Перехват всех ошибко на сервере.*/
    ServerExceptionMessage defaultExceptionHandler(HttpServletRequest hRequest, Exception exception);

    /**Перехват {@link ServerException} на сервере.*/
    ServerExceptionMessage serverExceptionHandler(HttpServletRequest hRequest, ServerException exception);

    /**Перехват {@link DataIntegrityViolationException} на сервере.*/
    /*ServerExceptionMessage sqlException(
            HttpServletRequest hRequest, DataIntegrityViolationException exception);*/

    /**Перехват {@link UnexpectedRequestParams}*/
    ServerExceptionMessage unExpectedParamExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, UnexpectedRequestParams exception);

    /**Перехват {@link NotFoundDataException}*/
    ServerExceptionMessage notFoundDataExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, NotFoundDataException exception);

    ServerExceptionMessage authExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, AuthenticationException exception);

    /**Перехват {@link AuthenticationException}*/
    ServerExceptionMessage forbiddenExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, ForbiddenException exception);

    /**Перехват {@link CustomHttpCodeException}*/
    ServerExceptionMessage customExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, CustomHttpCodeException exception);

    /**403*/
    ServerExceptionMessage forbidden(HttpServletRequest hRequest);

    /**401*/
    ServerExceptionMessage authorization(HttpServletRequest hRequest);

    /**404*/
    ServerExceptionMessage notFound(HttpServletRequest hRequest);
    ServerExceptionMessage notFound(NoHandlerFoundException ex, HttpServletRequest hRequest);

    /**Перехват ошибки, когда контроллер не поддрерживает http метод*/
    ServerExceptionMessage handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request);

    /**Перехват ошибки, когда контроллер не может прочитать объект*/
    ServerExceptionMessage handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request);

    default String logServerException(HttpServletRequest hRequest, ServerException exception) {
        log.error("Error code: {}", exception.getCode());

        if(StringUtils.hasText(exception.getDevelopMessage())) {
            log.error("Develop error message: {}", exception.getDevelopMessage());
            log.debug("Develop error object: {}", exception.getDevelopObject());
        }

        return this.logException(hRequest, exception);
    }

    default String logException(HttpServletRequest hRequest, Exception exception) {
        final String urlPath = hRequest.getRequestURI();
        final String logMessage = "Error on controller: '{}'";
        log.error(logMessage, urlPath, exception);

        return urlPath;
    }

    default String getOriginForwardUrlPath(HttpServletRequest request) {
        return (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
    }

    default String getOriginForwardUrlPath(WebRequest request) {
        return (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI,0);
    }

    ServerExceptionMessage handleValidationError(HttpServletRequest hRequest, MethodArgumentNotValidException exception);
}
