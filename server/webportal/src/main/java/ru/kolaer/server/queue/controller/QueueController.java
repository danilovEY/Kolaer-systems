package ru.kolaer.server.queue.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.core.model.dto.queue.PageQueueRequest;
import ru.kolaer.server.core.model.dto.queue.QueueRequestDto;
import ru.kolaer.server.core.model.dto.queue.QueueScheduleDto;
import ru.kolaer.server.core.model.dto.queue.QueueTargetDto;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.queue.service.QueueService;

@RestController
@RequestMapping(value = "/queue")
@Api(tags = "Очередь")
@Slf4j
public class QueueController {
    private final AuthenticationService authenticationService;
    private final QueueService queueService;

    @Autowired
    public QueueController(AuthenticationService authenticationService,
                           QueueService queueService) {
        this.authenticationService = authenticationService;
        this.queueService = queueService;
    }

    @ApiOperation(value = "Получить все цели")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<QueueTargetDto> getAllTarget(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return queueService.getAll(pageNum, pageSize);
    }

    @ApiOperation(value = "Получить все очереди у цели")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}/request", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<QueueRequestDto> getAllRequest(@PathVariable("id") Long id,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return queueService.getAllQueueRequestByTarget(id, pageNum, pageSize);
    }

    @ApiOperation(value = "Получить рассписание")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/scheduler", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<QueueScheduleDto> getAllRequest(@ModelAttribute PageQueueRequest request) {
        return queueService.getSchedulers(request);
    }

    @ApiOperation(value = "Добавить очередь к цели")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueRequestDto addRequest(@PathVariable("id") Long id,
                                      @RequestBody QueueRequestDto queueRequestDto) {
        return queueService.addQueueRequest(id, queueRequestDto);
    }

    @ApiOperation(value = "Обновить очередь")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{targetId}/request/{requestId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueRequestDto updateRequest(@PathVariable("targetId") Long targetId,
                                         @PathVariable("requestId") Long requestId,
                                         @RequestBody QueueRequestDto queueRequestDto) {
        return queueService.updateQueueRequest(targetId, requestId, queueRequestDto);
    }

    @ApiOperation(value = "Удалить очередь")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{targetId}/request/{requestId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateRequest(@PathVariable("targetId") Long targetId,
                              @PathVariable("requestId") Long requestId) {
        queueService.deleteQueueRequest(targetId, requestId);
    }

    @ApiOperation(value = "Добавить цель")
    @PreAuthorize("isAuthenticated()") //TODO: add role
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueTargetDto addQueueTarget(@RequestBody QueueTargetDto queueTargetDto) {
        return queueService.add(queueTargetDto);
    }

    @ApiOperation(value = "Обновить цель")
    @PreAuthorize("isAuthenticated()") //TODO: add role
    @RequestMapping(value = "/{targetId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueTargetDto updateQueueTarget(@PathVariable("targetId") Long targetId,
                                            @RequestBody QueueTargetDto queueTargetDto) {
        return queueService.update(targetId, queueTargetDto);
    }

    @ApiOperation(value = "Удалить цель")
    @PreAuthorize("isAuthenticated()") //TODO: add role
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteQueueTarget(@PathVariable("id") Long id) {
        queueService.delete(id);
    }
}
