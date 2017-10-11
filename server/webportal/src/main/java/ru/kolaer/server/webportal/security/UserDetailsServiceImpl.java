package ru.kolaer.server.webportal.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 18.07.2016.
 * Сервис позволяющий проверить пользолвателя на наличии в БД.
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountDao accountDao;

    public UserDetailsServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String login = Optional.ofNullable(username)
                .map(String::toLowerCase)
                .orElse("");

        AccountEntity account = accountDao.findName(login);
        if(account != null) {
               return new User(account.getUsername(), account.getPassword(),
                       true,true,true,true,
                       RoleUtils.roleToListString(account)
                               .stream()
                               .map(SimpleGrantedAuthority::new)
                               .collect(Collectors.toList()));
        }

        throw new UsernameNotFoundException("Пользователь: " + username + " не найден!");
    }
}
