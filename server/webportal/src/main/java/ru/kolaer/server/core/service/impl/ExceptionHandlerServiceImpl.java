package ru.kolaer.server.core.service.impl;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.common.dto.error.ServerExceptionMessage;
import ru.kolaer.server.core.exception.*;
import ru.kolaer.server.core.service.ExceptionHandlerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class ExceptionHandlerServiceImpl implements ExceptionHandlerService {

    @Override
    public ServerExceptionMessage defaultExceptionHandler(HttpServletRequest hRequest,
                                                          Exception exception) {
        String urlPath = this.logException(hRequest, exception);

        return new ServerExceptionMessage(SERVER_EXCEPTION_CODE, urlPath,
                exception.getMessage(), ErrorCode.SERVER_ERROR);
    }

    @Override
    public ServerExceptionMessage serverExceptionHandler(HttpServletRequest hRequest,
                                                         ServerException exception) {
        String urlPath = this.logServerException(hRequest, exception);

        return new ServerExceptionMessage(SERVER_EXCEPTION_CODE, exception.getCode(), urlPath,
                exception.getMessage(), exception.getDevelopMessage(),
                exception.getDevelopObject(),
                new Date());
    }

    @Override
    public ServerExceptionMessage unExpectedParamExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, UnexpectedRequestParams exception) {

        final String urlPath = this.logException(hRequest, exception);

        return new ServerExceptionMessage(BAD_REQUEST_CODE, exception.getCode(), urlPath,
                exception.getMessage(), exception.getDevelopMessage(),
                exception.getDevelopObject(),
                new Date());
    }

    @Override
    public ServerExceptionMessage notFoundDataExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, NotFoundDataException exception) {

        final String urlPath = this.logException(hRequest, exception);

        return new ServerExceptionMessage(NOT_FOUND_CODE, exception.getCode(), urlPath,
                exception.getMessage(), exception.getDevelopMessage(),
                exception.getDevelopObject(),
                new Date());
    }

    @Override
    public ServerExceptionMessage authExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, AuthenticationException exception) {

        final String urlPath = this.logException(hRequest, exception);

        return new ServerExceptionMessage(UNAUTHORIZED_CODE, urlPath, Optional.ofNullable(exception.getMessage()).orElse("Неправильный логин или пароль"),
                ErrorCode.UNAUTHORIZED);
    }

    @Override
    public ServerExceptionMessage forbiddenExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, ForbiddenException exception) {

        String urlPath = this.logException(hRequest, exception);

        return new ServerExceptionMessage(FORBIDDEN_CODE, exception.getCode(), urlPath,
                exception.getMessage(), exception.getDevelopMessage(),
                exception.getDevelopObject(),
                new Date());
    }

    @Override
    public ServerExceptionMessage forbiddenExceptionHandler(HttpServletRequest hRequest, HttpServletResponse hResponse, AccessDeniedException exception) {
        String urlPath = this.logException(hRequest, exception);

        return new ServerExceptionMessage(FORBIDDEN_CODE, ErrorCode.FORBIDDEN, urlPath,
                exception.getMessage(), null, null,
                new Date());
    }

    @Override
    public ServerExceptionMessage customExceptionHandler(
            HttpServletRequest hRequest, HttpServletResponse hResponse, CustomHttpCodeException exception) {

        if(exception.getHttpStatus() == null)
            return this.serverExceptionHandler(hRequest, exception);

        final String urlPath = this.logException(hRequest, exception);

        hResponse.setStatus(exception.getHttpStatus().value());

        return new ServerExceptionMessage(exception.getHttpStatus().value(),
                exception.getCode(),
                urlPath, exception.getMessage(),
                exception.getDevelopMessage(), exception.getDevelopObject(), new Date());
    }

    @Override
    public ServerExceptionMessage forbidden(HttpServletRequest hRequest) {
        final String originalUri = this.getOriginForwardUrlPath(hRequest);

        return new ServerExceptionMessage(FORBIDDEN_CODE, originalUri,
                FORBIDDEN_MESSAGE, ErrorCode.FORBIDDEN);
    }

    @Override
    public ServerExceptionMessage authorization(HttpServletRequest hRequest) {
        final String originalUri = this.getOriginForwardUrlPath(hRequest);

        return new ServerExceptionMessage(UNAUTHORIZED_CODE, originalUri,
                UNAUTHORIZED_MESSAGE, ErrorCode.UNAUTHORIZED);
    }

    @Override
    public ServerExceptionMessage notFound(HttpServletRequest hRequest) {
        final String originalUri = this.getOriginForwardUrlPath(hRequest);

        final String logMessage = "Контроллер '" + originalUri + "' не найден!";

        log.error(logMessage);

        return new ServerExceptionMessage(NOT_FOUND_CODE, originalUri, logMessage,
                ErrorCode.CONTROLLER_NOT_FOUND);
    }

    @Override
    public ServerExceptionMessage notFound(NoHandlerFoundException ex, HttpServletRequest hRequest) {
        final String originalUri = ex.getRequestURL();

        final String logMessage = "Контроллер '" + originalUri + "' не найден!";

        log.error(logMessage);

        return new ServerExceptionMessage(NOT_FOUND_CODE, originalUri, logMessage,
                ErrorCode.CONTROLLER_NOT_FOUND);
    }

    @Override
    public ServerExceptionMessage handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                      WebRequest request) {
        final String originalUri = this.getOriginForwardUrlPath(request);

        final String logMessage = "Контроллер '" + originalUri + "' не поддерживает метод запроса или не найден!";

        log.error(logMessage);

        return new ServerExceptionMessage(METHOD_NOT_ALLOWED, originalUri, logMessage,
                ex.getMessage(),
                null,
                new Date());
    }

    @Override
    public ServerExceptionMessage handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               WebRequest request) {
        return new ServerExceptionMessage(BAD_REQUEST_CODE, "",
                BAD_REQUEST_MESSAGE, ex.getCause().getMessage(),
                null,
                new Date());
    }

    @Override
    public ServerExceptionMessage handleValidationError(HttpServletRequest hRequest, MethodArgumentNotValidException exception) {
        String urlPath = this.logException(hRequest, exception);

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
        if (bindingResult.hasGlobalErrors()) {
            for (ObjectError error : bindingResult.getGlobalErrors()) {
                fieldErrors.add(new FieldError(error.getObjectName(), error.getObjectName(), error.getDefaultMessage()));
            }
        }

        Map<String, List<String>> map = this.fieldErrorsToMap(fieldErrors);

        return new ServerExceptionMessage(ExceptionHandlerService.BAD_REQUEST_CODE, ErrorCode.INCORRECT_REQUEST_VALUE,
                urlPath, exception.getMessage(), null, map, new Date());
    }

    private Map<String, List<String>> fieldErrorsToMap(List<FieldError> errors) {
        Map<String, List<String>> fieldsErrors = new HashMap<>();
        for (FieldError error : errors) {
            List<String> fieldErrors = fieldsErrors.computeIfAbsent(error.getField(), s -> new ArrayList<>());
            fieldErrors.add(error.getDefaultMessage());
        }
        return fieldsErrors;
    }
}
