package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Danilov on 24.07.2016.
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);
        LOG.debug("Date: {}", httpRequest.getHeaderNames().nextElement());

        chain.doFilter(request, response);
    }

    private HttpServletRequest getAsHttpRequest(ServletRequest request)
    {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }

        return (HttpServletRequest) request;
    }
}
