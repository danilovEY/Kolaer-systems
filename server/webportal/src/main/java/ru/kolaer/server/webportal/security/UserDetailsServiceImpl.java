package ru.kolaer.server.webportal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dao.UserDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntity;

import java.util.stream.Collectors;

/**
 * Created by danilovey on 18.07.2016.
 * Сервис позволяющий проверить пользолвателя на наличии в БД.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userResult = null;

        for(GeneralAccountsEntity acc : userDao.findAll()) {
           if(acc.getUsername().equals(username)){
               userResult = new User(username, acc.getPassword(), true,true,true,true, acc.getRoles().stream().map(role -> {
                   return new SimpleGrantedAuthority(role.getType().toString());
               }).collect(Collectors.toList()));
               break;
           }
        }

        if(userResult == null) {
            throw new UsernameNotFoundException(username + " is not found user!");
        }

        return userResult;
    }
}
