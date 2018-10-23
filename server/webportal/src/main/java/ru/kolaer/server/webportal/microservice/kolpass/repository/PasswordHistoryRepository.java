package ru.kolaer.server.webportal.microservice.kolpass.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.kolpass.entity.PasswordHistoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordHistoryRepository extends DefaultRepository<PasswordHistoryEntity> {
    Long findCountHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    List<PasswordHistoryEntity> findHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    List<PasswordHistoryEntity> findAllHistoryByIdRepository(Long id);

    void deleteAllByIdRep(Long repId);

    PasswordHistoryEntity findByRepAndId(Long repId, Long passId);

    PasswordHistoryEntity findLastHistoryInRepository(Long repId);

}
