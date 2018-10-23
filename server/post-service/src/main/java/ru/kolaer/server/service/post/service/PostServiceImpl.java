package ru.kolaer.server.service.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.service.post.dto.PostRequestDto;
import ru.kolaer.server.webportal.common.exception.NotFoundDataException;
import ru.kolaer.server.webportal.common.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.service.post.converter.PostConverter;
import ru.kolaer.server.service.post.dto.FindPostPageRequest;
import ru.kolaer.server.service.post.entity.PostEntity;
import ru.kolaer.server.service.post.repository.PostRepository;

import java.util.List;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PostServiceImpl
        extends AbstractDefaultService<PostDto, PostEntity, PostRepository, PostConverter>
        implements PostService {

    protected PostServiceImpl(PostRepository defaultEntityDao, PostConverter converter) {
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
    public Page<PostDto> find(FindPostPageRequest request) {
        long count = defaultEntityDao.findCount(request);
        List<PostDto> posts = defaultConverter.convertToDto(defaultEntityDao.find(request));

        return new Page<>(posts, request.getNumber(), count, request.getPageSize());
    }
}
