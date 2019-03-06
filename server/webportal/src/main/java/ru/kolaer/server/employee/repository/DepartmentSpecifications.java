package ru.kolaer.server.employee.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import ru.kolaer.server.core.repository.BaseSpecification;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;
import ru.kolaer.server.employee.model.entity.DepartmentEntity_;
import ru.kolaer.server.employee.model.request.FindDepartmentPageRequest;

import javax.validation.constraints.NotNull;

@Slf4j
public class DepartmentSpecifications extends BaseSpecification {

    public static Specification<DepartmentEntity> findAll(@NotNull FindDepartmentPageRequest request) {
        Specification<DepartmentEntity> specification = Specification
                .where(findByName(request.getQuery()))
                .or(findByAbbreviatedName(request.getQuery()));

        try {
            specification = specification.or(findByCode(Integer.valueOf(request.getQuery())));
        } catch (NumberFormatException ignore) { }

        return Specification.where(specification).and(deleted(request.getDeleted()));
    }


    public static Specification<DepartmentEntity> findByName(@Nullable String name) {
        return StringUtils.hasText(name)
                ? (root, query, criteriaBuilder) -> criteriaBuilder.like(
                        root.get(DepartmentEntity_.name), containsToLowerCase(name))
                : null;
    }

    public static Specification<DepartmentEntity> findByAbbreviatedName(@Nullable String abbreviatedName) {
        return StringUtils.hasText(abbreviatedName)
                ? (root, query, criteriaBuilder) -> criteriaBuilder.like(
                        root.get(DepartmentEntity_.abbreviatedName), containsToLowerCase(abbreviatedName))
                : null;
    }

    public static Specification<DepartmentEntity> findByCode(@Nullable Integer code) {
        return code != null
                ? (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DepartmentEntity_.code), code)
                : null;
    }

    public static Specification<DepartmentEntity> deleted(@Nullable Boolean deleted) {
        return deleted != null
                ? (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DepartmentEntity_.deleted), deleted)
                : null;
    }

}
