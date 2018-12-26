package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.employee.model.entity.PostEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class PostConverterImpl implements PostConverter {

    @Override
    public PostEntity convertToModel(PostDto dto) {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(dto.getId());
        postEntity.setAbbreviatedName(dto.getAbbreviatedName());
        postEntity.setName(dto.getName());
        postEntity.setRang(dto.getRang());
        postEntity.setType(dto.getType());
        postEntity.setCode(dto.getCode());

        return postEntity;
    }

    @Override
    public PostDto convertToDto(PostEntity model) {
        PostDto postDto = new PostDto();
        postDto.setId(model.getId());
        postDto.setAbbreviatedName(model.getAbbreviatedName());
        postDto.setName(model.getName());
        postDto.setRang(model.getRang());
        postDto.setType(model.getType());
        postDto.setCode(model.getCode());

        return postDto;
    }

    @Override
    public PostDto updateData(PostDto oldDto, PostEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setAbbreviatedName(newModel.getAbbreviatedName());
        oldDto.setName(newModel.getName());
        oldDto.setRang(newModel.getRang());
        oldDto.setType(newModel.getType());
        oldDto.setCode(newModel.getCode());

        return oldDto;
    }
}
