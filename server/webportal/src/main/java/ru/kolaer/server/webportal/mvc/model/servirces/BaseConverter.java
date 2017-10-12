package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface BaseConverter<T extends BaseDto, K extends BaseEntity> {
    K convertToModel(T dto);
    T convertToDto(K model);
    T updateData(T oldDto, K newModel);

    default T convertToDtoWithOutSubEntity(K model){
        return convertToDto(model);
    }
}
