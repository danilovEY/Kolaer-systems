package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.employee.converter.PostConverter;
import ru.kolaer.server.employee.dao.PostDao;
import ru.kolaer.server.employee.model.dto.PostRequestDto;
import ru.kolaer.server.employee.model.entity.PostEntity;
import ru.kolaer.server.employee.model.request.FindPostPageRequest;

import java.util.List;

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
    @Transactional
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
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public PageDto<PostDto> find(FindPostPageRequest request) {
        long count = defaultEntityDao.findCount(request);
        List<PostDto> posts = defaultConverter.convertToDto(defaultEntityDao.find(request));

        return new PageDto<>(posts, request.getPageNum(), count, request.getPageSize());
    }
}
