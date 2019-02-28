package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.employee.model.dto.PostRequestDto;
import ru.kolaer.server.employee.model.request.FindPostPageRequest;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostService extends DefaultService<PostDto> {
    PostDto add(PostRequestDto postRequestDto);

    PostDto update(Long postId, PostRequestDto postRequestDto);

    PageDto<PostDto> find(FindPostPageRequest request);
}
