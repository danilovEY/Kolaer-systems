package ru.kolaer.server.businesstrip.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.businesstrip.model.entity.BusinessTripEmployeeEntity;
import ru.kolaer.server.core.repository.BaseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessTripEmployeeRepository extends BaseRepository<BusinessTripEmployeeEntity> {

    List<BusinessTripEmployeeEntity> findAllByBusinessTripIdIsIn(Collection<Long> businessTripIds);
    void deleteAllByBusinessTripId(long businessTripId);

    Optional<BusinessTripEmployeeEntity> findByBusinessTripIdAndEmployeeId(long businessTripId, long employeeId);
}
