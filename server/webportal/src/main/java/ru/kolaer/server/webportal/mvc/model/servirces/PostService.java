package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.dto.PostRequestDto;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostService extends DefaultService<PostDto> {
    PostDto add(PostRequestDto postRequestDto);

    PostDto update(Long postId, PostRequestDto postRequestDto);

}
