package ru.kolaer.server.kolpass.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.kolpass.model.entity.PasswordHistoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface PasswordHistoryDao extends DefaultDao<PasswordHistoryEntity> {
    Long findCountHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    List<PasswordHistoryEntity> findHistoryByIdRepository(Long id, Integer number, Integer pageSize);

    List<PasswordHistoryEntity> findAllHistoryByIdRepository(Long id);

    void deleteAllByIdRep(Long repId);

    PasswordHistoryEntity findByRepAndId(Long repId, Long passId);

    PasswordHistoryEntity findLastHistoryInRepository(Long repId);

}
