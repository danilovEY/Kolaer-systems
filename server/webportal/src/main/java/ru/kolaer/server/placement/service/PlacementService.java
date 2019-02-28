package ru.kolaer.server.placement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.core.model.dto.PaginationRequest;
import ru.kolaer.server.core.model.dto.placement.PlacementDto;
import ru.kolaer.server.placement.model.entity.PlacementEntity;
import ru.kolaer.server.placement.repository.PlacementRepository;

import javax.validation.constraints.NotNull;

@Service
public class PlacementService {
    private final PlacementRepository placementRepository;
    private final PlacementConverter placementConverter;

    @Autowired
    public PlacementService(PlacementRepository placementRepository, PlacementConverter placementConverter) {
        this.placementRepository = placementRepository;
        this.placementConverter = placementConverter;
    }

    @Transactional(readOnly = true)
    public PageDto<PlacementDto> findAll(@NotNull PaginationRequest request) {
        Page<PlacementEntity> page = placementRepository.findAll(request.toPageRequest());

        return new PageDto<>(
                placementConverter.convertToDto(page.getContent()),
                request.getPageNum(),
                page.getTotalPages(),
                request.getPageSize()
        );
    }

}
