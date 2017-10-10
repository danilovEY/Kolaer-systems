package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.server.webportal.mvc.model.entities.other.CounterEntity;

/**
 * Created by danilovey on 10.10.2017.
 */
@Service
public class CounterConverterImpl implements CounterConverter {
    @Override
    public CounterEntity convertToModel(CounterDto dto) {
        CounterEntity counterEntity = new CounterEntity();
        counterEntity.setId(dto.getId());
        counterEntity.setDescription(dto.getDescription());
        counterEntity.setEnd(dto.getEnd());
        counterEntity.setStart(dto.getStart());
        counterEntity.setTitle(dto.getTitle());
        return counterEntity;
    }

    @Override
    public CounterDto convertToDto(CounterEntity model) {
        CounterDto counterDto = new CounterDto();
        counterDto.setId(model.getId());
        counterDto.setDescription(model.getDescription());
        counterDto.setEnd(model.getEnd());
        counterDto.setStart(model.getStart());
        counterDto.setTitle(model.getTitle());
        return counterDto;
    }

    @Override
    public CounterDto updateData(CounterDto oldDto, CounterEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setDescription(newModel.getDescription());
        oldDto.setEnd(newModel.getEnd());
        oldDto.setStart(newModel.getStart());
        oldDto.setTitle(newModel.getTitle());
        return oldDto;
    }
}
