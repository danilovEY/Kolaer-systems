package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.employee.model.dto.PostRequestDto;
import ru.kolaer.server.employee.model.request.FindPostPageRequest;
import ru.kolaer.server.employee.model.request.PostFilter;
import ru.kolaer.server.employee.model.request.PostSort;
import ru.kolaer.server.employee.service.PostService;

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
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<PostDto> getAllDepartment(@RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                          @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                          PostSort sortParam,
                                          PostFilter filter) {
        return this.postService.getAll(sortParam, filter, pageNum, pageSize);
    }

    @ApiOperation(value = "Найти должности")
    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<PostDto> getAllDepartment(FindPostPageRequest request) {
        return this.postService.find(request);
    }

    @ApiOperation(value = "Добавить должность")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.POSTS_WRITE + "')")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PostDto addPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.add(postRequestDto);
    }

    @ApiOperation(value = "Обновит должность")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.POSTS_WRITE + "')")
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PostDto updatePost(@PathVariable("postId") Long postId,
                                           @RequestBody PostRequestDto postRequestDto) {
        return postService.update(postId, postRequestDto);
    }

    @ApiOperation(value = "Удалить должность")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.POSTS_WRITE + "')")
    @RequestMapping(value = "/{depId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId, true);
    }

}
