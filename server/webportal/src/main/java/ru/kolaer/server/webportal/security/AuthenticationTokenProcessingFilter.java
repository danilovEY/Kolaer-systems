package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
 * Фильтер позволяющий динамически проверять соединения на наличие правильных токенов.
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);

    private UserDetailsService userDetailsService;

    public AuthenticationTokenProcessingFilter(UserDetailsService userService) {
        this.userDetailsService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);

        String authToken = httpRequest.getParameter("token");
        if(authToken != null) {
            String userName = authToken.split(":")[0];
            LOG.info(authToken);
            if (userName != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                if(userDetails != null) {
                    LOG.info(userDetails.getPassword());
                    if (authToken.split(":")[1].equals(userDetails.getPassword())) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }


        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    private HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }

        return (HttpServletRequest) request;
    }
}
