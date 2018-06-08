package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.PostConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.dto.PostRequestDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.PostService;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PostServiceImpl
        extends AbstractDefaultService<PostDto, PostEntity, PostDao, PostConverter>
        implements PostService {

    protected PostServiceImpl(PostDao defaultEntityDao, PostConverter converter) {
        super(defaultEntityDao, converter);
    }

    @Override
    public PostDto add(PostRequestDto postRequestDto) {
        if (postRequestDto == null ||
                StringUtils.isEmpty(postRequestDto.getName()) ||
                StringUtils.isEmpty(postRequestDto.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("Имя н едолжно быть пустым");
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setName(postRequestDto.getName());
        postEntity.setAbbreviatedName(postRequestDto.getAbbreviatedName());
        postEntity.setCode(postRequestDto.getCode());
        postEntity.setRang(postRequestDto.getRang());
        postEntity.setType(postRequestDto.getType());

        return defaultConverter.convertToDto(defaultEntityDao.save(postEntity));
    }

    @Override
    public PostDto update(Long postId, PostRequestDto postRequestDto) {
        if (postId == null || postRequestDto == null ||
                StringUtils.isEmpty(postRequestDto.getName()) ||
                StringUtils.isEmpty(postRequestDto.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("Имя н едолжно быть пустым");
        }

        PostEntity postEntity = defaultEntityDao.findById(postId);
        if (postEntity == null) {
            throw new NotFoundDataException("Должность не найдена");
        }

        postEntity.setName(postRequestDto.getName());
        postEntity.setAbbreviatedName(postRequestDto.getAbbreviatedName());
        postEntity.setCode(postRequestDto.getCode());
        postEntity.setRang(postRequestDto.getRang());
        postEntity.setType(postRequestDto.getType());

        return defaultConverter.convertToDto(defaultEntityDao.save(postEntity));
    }
}
