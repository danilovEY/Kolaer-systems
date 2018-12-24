package ru.kolaer.server.webportal.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;
import ru.kolaer.server.webportal.service.impl.TokenService;

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
@Slf4j
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
    private final ServerAuthType serverAuthType;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    public AuthenticationTokenProcessingFilter(ServerAuthType serverAuthType,
                                               UserDetailsService userDetailsService,
                                               TokenService tokenService) {
        this.serverAuthType = serverAuthType;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);

        String authToken = this.extractAuthTokenFromRequest(httpRequest);
        String userName = tokenService.getUserNameFromToken(authToken);
        if (userName != null) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userDetails != null){
                if (tokenIsValidate(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    private boolean tokenIsValidate(String authToken, UserDetails userDetails) {
        switch (serverAuthType) {
            case LDAP: return tokenService.validateTokenLDAP(authToken, userDetails);
            default: return tokenService.validateToken(authToken, userDetails);
        }
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
