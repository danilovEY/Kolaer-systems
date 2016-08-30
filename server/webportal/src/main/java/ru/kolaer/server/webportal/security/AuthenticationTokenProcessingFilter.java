package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Фильтер позволяющий динамически проверять соединения и предостовлять доступ к url
 * по наличию токена.
 *
 * Created by Danilov on 24.07.2016.
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);

    private UserDetailsService userDetailsService;
    private final boolean isLDAP;

    public AuthenticationTokenProcessingFilter(UserDetailsService userService, boolean isLDAP) {
        this.userDetailsService = userService;
        this.isLDAP = isLDAP;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);

        String authToken = this.extractAuthTokenFromRequest(httpRequest);
        String userName = TokenUtils.getUserNameFromToken(authToken);
        if (userName != null) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if(userDetails != null){
                boolean tokenVal = this.isLDAP ? TokenUtils.validateTokenLDAP(authToken, userDetails) : TokenUtils.validateToken(authToken, userDetails);
                if (tokenVal) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
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

    /**Получить токен из http-запроса.*/
    private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
        String authToken = httpRequest.getHeader("x-token");

        if (authToken == null) {
            authToken = httpRequest.getParameter("token");
        }

        return authToken;
    }
}
