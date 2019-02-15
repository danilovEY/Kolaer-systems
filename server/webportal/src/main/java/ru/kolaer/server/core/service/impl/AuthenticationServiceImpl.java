package ru.kolaer.server.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.service.AuthenticationService;

import javax.validation.constraints.NotEmpty;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
@Slf4j
@Validated
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public boolean isAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !auth.getName().equals("anonymousUser");
    }

    @Override
    public AccountAuthorizedDto getAccountAuthorized() {
        if (!isAuth()) throw new AuthenticationServiceException("Вы не авторизованы");

        return (AccountAuthorizedDto) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public boolean containsAccess(@NotEmpty String access) {
        return isAuth() && SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(access));
    }

}
