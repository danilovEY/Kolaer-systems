package ru.kolaer.server.service.post.service;

import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.service.post.dto.PostRequestDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.service.post.dto.FindPostPageRequest;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostService extends DefaultService<PostDto> {
    PostDto add(PostRequestDto postRequestDto);

    PostDto update(Long postId, PostRequestDto postRequestDto);

    Page<PostDto> find(FindPostPageRequest request);
}
