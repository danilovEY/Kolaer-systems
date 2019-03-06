package ru.kolaer.server.contact.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.contact.model.entity.ContactEntity;
import ru.kolaer.server.core.repository.BaseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends BaseRepository<ContactEntity> {

    Optional<ContactEntity> findByEmployeeId(long employeeId);
    List<ContactEntity> findByEmployeeIdIsIn(Collection<Long> employeeIds);

//    @Query("FROM ContactEntity WHERE email LIKE %:#{request.query}% OR " +
//            "mobilePhoneNumber LIKE %:searchText% OR " +
//            "workPhoneNumber LIKE %:searchText% OR " +
//            "pager LIKE %:searchText% OR " +
//            "placementId IN (:placeIds)")
//    List<ContactEntity> findByEmployeeIds(@Param("request") FindContactPageRequest request);
}
