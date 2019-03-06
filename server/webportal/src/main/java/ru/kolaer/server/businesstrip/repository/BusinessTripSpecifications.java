package ru.kolaer.server.businesstrip.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.kolaer.server.businesstrip.model.dto.request.FindBusinessTripRequest;
import ru.kolaer.server.businesstrip.model.entity.BusinessTripEntity;

public class BusinessTripSpecifications {

    public static Specification<BusinessTripEntity> findAll(FindBusinessTripRequest request) {
        return (root, query, criteriaBuilder) -> null;
    }

}
