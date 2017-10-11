package ru.kolaer.server.webportal.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.server.webportal.mvc.model.servirces.ExceptionHandlerService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by danilovey on 11.10.2017.
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ExceptionHandlerService exceptionHandlerService;
    private final ObjectMapper objectMapper;

    public ExceptionHandlerFilter(ExceptionHandlerService exceptionHandlerService) {
        this.exceptionHandlerService = exceptionHandlerService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException ex) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());

            ServerExceptionMessage serverExceptionMessage = exceptionHandlerService
                    .authExceptionHandler(request, response, ex);

            objectMapper.writeValue(response.getWriter(), serverExceptionMessage);
        } catch(RuntimeException ex) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            ServerExceptionMessage serverExceptionMessage = exceptionHandlerService
                    .defaultExceptionHandler(request, ex);

            objectMapper.writeValue(response.getWriter(), serverExceptionMessage);
        }
    }
}
