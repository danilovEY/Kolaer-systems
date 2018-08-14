package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.post.PostFilter;
import ru.kolaer.server.webportal.mvc.model.dto.post.PostRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.post.PostSort;
import ru.kolaer.server.webportal.mvc.model.servirces.PostService;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Должности")
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Получить все должности")
    @UrlDeclaration(description = "Получить все должности")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PostDto> getAllDepartment(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                          @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                          PostSort sortParam,
                                          PostFilter filter) {
        return this.postService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Добавить должность")
    @UrlDeclaration(description = "Добавить должность", isUser = false, requestMethod = RequestMethod.POST, isOk = true)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PostDto addPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.add(postRequestDto);
    }

    @ApiOperation(value = "Обновит должность")
    @UrlDeclaration(description = "Обновит должность", isUser = false, requestMethod = RequestMethod.PUT, isOk = true)
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PostDto updatePost(@PathVariable("postId") Long postId,
                                           @RequestBody PostRequestDto postRequestDto) {
        return postService.update(postId, postRequestDto);
    }

    @ApiOperation(value = "Удалить должность")
    @UrlDeclaration(description = "Удалить должность", isUser = false, requestMethod = RequestMethod.DELETE, isOk = true)
    @RequestMapping(value = "/{depId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId, true);
    }

}
