package ru.kolaer.server.service.holiday.converter;

import org.springframework.stereotype.Component;
import ru.kolaer.server.service.holiday.dto.HolidayDto;
import ru.kolaer.server.service.holiday.entity.HolidayEntity;

@Component
public class HolidayConverterImpl implements HolidayConverter {
    @Override
    public HolidayEntity convertToModel(HolidayDto dto) {
        if(dto == null) {
            return null;
        }

        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setId(dto.getId());
        holidayEntity.setHolidayDate(dto.getHolidayDate());
        holidayEntity.setHolidayType(dto.getHolidayType());
        holidayEntity.setName(dto.getName());
        return holidayEntity;
    }

    @Override
    public HolidayDto convertToDto(HolidayEntity model) {
        return updateData(new HolidayDto(), model);
    }

    @Override
    public HolidayDto updateData(HolidayDto oldDto, HolidayEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setHolidayDate(newModel.getHolidayDate());
        oldDto.setHolidayType(newModel.getHolidayType());
        oldDto.setName(newModel.getName());

        return oldDto;
    }
}
