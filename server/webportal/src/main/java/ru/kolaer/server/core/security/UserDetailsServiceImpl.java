package ru.kolaer.server.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.account.dao.AccountDao;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.account.model.entity.AccountEntity;
import ru.kolaer.server.account.repository.AccountAccessRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by danilovey on 18.07.2016.
 * Сервис позволяющий проверить пользолвателя на наличии в БД.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountDao accountDao;
    private final AccountAccessRepository accountAccessRepository;

    public UserDetailsServiceImpl(AccountDao accountDao,
            AccountAccessRepository accountAccessRepository) {
        this.accountDao = accountDao;
        this.accountAccessRepository = accountAccessRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AccountEntity account = Optional.ofNullable(username)
                .map(String::toLowerCase)
                .map(accountDao::findName)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь: " + username + " не найден!"));

//        if(account.isBlock()) {
//            throw new UserIsBlockException();
//        }

//        Set<String> accessNames = new HashSet<>(accountAccessRepository.findByAccountId(account.getId()));
        Set<String> accessNames = new HashSet<>(account.getAccess());

        return new AccountAuthorizedDto(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                account.getEmployeeId(),
                accessNames,
                !account.isBlock()
        );
    }
}
