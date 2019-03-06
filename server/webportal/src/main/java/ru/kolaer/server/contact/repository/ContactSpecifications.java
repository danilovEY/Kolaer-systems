package ru.kolaer.server.contact.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.server.contact.model.entity.ContactEntity;
import ru.kolaer.server.contact.model.entity.ContactEntity_;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.contact.model.request.FindContactPageRequest;
import ru.kolaer.server.core.repository.BaseSpecification;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public class ContactSpecifications extends BaseSpecification {

    public static Specification<ContactEntity> findByEmployeeIds(@NotNull Collection<Long> employeeIds) {
        return CollectionUtils.isEmpty(employeeIds)
                ? null
                : (root, query, criteriaBuilder) -> root.get(ContactEntity_.employeeId).in(employeeIds);
    }

    public static Specification<ContactEntity> findByPlacementIds(@NotNull Collection<Long> placementIds) {
        return CollectionUtils.isEmpty(placementIds)
                ? null
                : (root, query, criteriaBuilder) -> root.get(ContactEntity_.placementId).in(placementIds);
    }

    public static Specification<ContactEntity> findByWorkPhoneNumber(@Nullable String text) {
        return StringUtils.hasText(text)
                ? (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(ContactEntity_.workPhoneNumber), containsToLowerCase(text))
                : null;
    }

    public static Specification<ContactEntity> findByMobilePhoneNumber(@Nullable String text) {
        return StringUtils.hasText(text)
                ? (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(ContactEntity_.mobilePhoneNumber), containsToLowerCase(text))
                : null;
    }

    public static Specification<ContactEntity> findByEmail(@Nullable String text) {
        return StringUtils.hasText(text)
                ? (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(ContactEntity_.email), containsToLowerCase(text))
                : null;
    }

    public static Specification<ContactEntity> findByPager(@Nullable String text) {
        return StringUtils.hasText(text)
                ? (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(ContactEntity_.pager), containsToLowerCase(text))
                : null;
    }

    public static Specification<ContactEntity> findByType(@Nullable ContactType type) {
        return type != null
                ? (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ContactEntity_.type), type)
                : null;
    }

    public static Specification<ContactEntity> findContacts(@NotNull FindContactPageRequest request) {
        return Specification.where(Specification.where(findByEmployeeIds(request.getEmployeeIds()))
                .or(findByPlacementIds(request.getPlacementIds()))
                .or(findByEmail(request.getQuery()))
                .or(findByPager(request.getQuery()))
                .or(findByWorkPhoneNumber(request.getQuery()))
                .or(findByMobilePhoneNumber(request.getQuery()))
        ).and(findByType(request.getType()));
    }

}
