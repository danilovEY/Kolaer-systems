package ru.kolaer.server.webportal.microservice.kolpass;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordRepositoryRepository extends DefaultRepository<PasswordRepositoryEntity> {
    Long findCountAllAccountId(Long pnumber, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByAccountId(Long accountId, Integer number, Integer pageSize);

    List<PasswordRepositoryEntity> findAllByAccountId(List<Long> idsAccount);

    boolean shareRepositoryToAccount(Long repId, Long accountId);
    int deleteShareRepositoryToAccount(Long repId, Long accountId);

    List<Long> findAllAccountFromShareRepository(Long repId);

    List<Long> findAllRepositoryFromShare(Long accountId);
}
