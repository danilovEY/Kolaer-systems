package ru.kolaer.server.webportal.microservice.placement;

import org.springframework.stereotype.Component;

@Component
public class PlacementConverterImpl implements PlacementConverter {
    @Override
    public PlacementEntity convertToModel(PlacementDto dto) {
        if(dto == null) {
            return null;
        }

        PlacementEntity placementEntity = new PlacementEntity();
        placementEntity.setId(dto.getId());
        placementEntity.setName(dto.getName());
        return placementEntity;
    }

    @Override
    public PlacementDto convertToDto(PlacementEntity model) {
        return updateData(new PlacementDto(), model);
    }

    @Override
    public PlacementDto updateData(PlacementDto oldDto, PlacementEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setName(newModel.getName());
        return oldDto;
    }
}
