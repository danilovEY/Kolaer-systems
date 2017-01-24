package ru.kolaer.server.webportal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;

import java.util.stream.Collectors;

/**
 * Created by danilovey on 18.07.2016.
 * Сервис позволяющий проверить пользолвателя на наличии в БД.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userResult = null;

        for(AccountEntity acc : accountDao.findAll()) {
           if(acc.getUsername().equals(username)){
               userResult = new User(username, acc.getPassword(), true,true,true,true,
                       acc.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getType()))
                       .collect(Collectors.toList()));
               break;
           }
        }

        if(userResult == null) {
            throw new UsernameNotFoundException(username + " is not found user!");
        }

        return userResult;
    }
}
