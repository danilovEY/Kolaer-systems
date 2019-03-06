package ru.kolaer.server.businesstrip.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.businesstrip.model.dto.responce.BusinessTripDto;

@Service
@Validated
public class BusinessTripMapper {

    public BusinessTripDto mapToBusinessTripDto() {
        return new BusinessTripDto();
    }

}
