package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryDao extends DefaultDao<PasswordRepositoryEntity> {
    Long findCountAllAccountId(Long pnumber, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByAccountId(Long accountId, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByAccountId(List<Long> idsAccount);
}
