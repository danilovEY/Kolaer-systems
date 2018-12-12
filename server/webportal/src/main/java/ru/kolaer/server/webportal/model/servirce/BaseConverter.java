package ru.kolaer.server.webportal.model.servirce;

import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface BaseConverter<T extends BaseDto, K extends BaseEntity> {
    K convertToModel(T dto);
    T convertToDto(K model);

    T updateData(T oldDto, K newModel);

    default List<K> convertToModel(List<T> dto) {
        if(dto == null || dto.isEmpty()) {
            return Collections.emptyList();
        }

        return dto.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    default List<T> convertToDto(List<K> model){
        if(model == null || model.isEmpty()) {
            return Collections.emptyList();
        }

        return model.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    default T convertToDtoWithOutSubEntity(K model){
        return convertToDto(model);
    }
}
