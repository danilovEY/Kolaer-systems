package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.webportal.model.entity.kolpass.PasswordRepositoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryDao extends DefaultDao<PasswordRepositoryEntity> {
    Long findCountAllAccountId(Long pnumber, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByAccountId(Long accountId, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByAccountId(List<Long> idsAccount);

    boolean shareRepositoryToAccount(Long repId, Long accountId);
    int deleteShareRepositoryToAccount(Long repId, Long accountId);

    List<Long> findAllAccountFromShareRepository(Long repId);

    List<Long> findAllRepositoryFromShare(Long accountId);
}
