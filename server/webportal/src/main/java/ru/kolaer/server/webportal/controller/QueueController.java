package ru.kolaer.server.webportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.Page;
import ru.kolaer.server.webportal.annotation.UrlDeclaration;
import ru.kolaer.server.webportal.model.dto.queue.PageQueueRequest;
import ru.kolaer.server.webportal.model.dto.queue.QueueRequestDto;
import ru.kolaer.server.webportal.model.dto.queue.QueueScheduleDto;
import ru.kolaer.server.webportal.model.dto.queue.QueueTargetDto;
import ru.kolaer.server.webportal.service.AuthenticationService;
import ru.kolaer.server.webportal.service.QueueService;

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
    @UrlDeclaration
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<QueueTargetDto> getAllTarget(@RequestParam(value = "page", defaultValue = "1") Integer number,
                                             @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return queueService.getAll(number, pageSize);
    }

    @ApiOperation(value = "Получить все очереди у цели")
    @UrlDeclaration
    @RequestMapping(value = "/{id}/request", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<QueueRequestDto> getAllRequest(@PathVariable("id") Long id,
                                               @RequestParam(value = "page", defaultValue = "1") Integer number,
                                               @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return queueService.getAllQueueRequestByTarget(id, number, pageSize);
    }

    @ApiOperation(value = "Получить рассписание")
    @UrlDeclaration
    @RequestMapping(value = "/scheduler", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<QueueScheduleDto> getAllRequest(@ModelAttribute PageQueueRequest request) {
        return queueService.getSchedulers(request);
    }

    @ApiOperation(value = "Добавить очередь к цели")
    @UrlDeclaration
    @RequestMapping(value = "/{id}/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueRequestDto addRequest(@PathVariable("id") Long id,
                                      @RequestBody QueueRequestDto queueRequestDto) {
        return queueService.addQueueRequest(id, queueRequestDto);
    }

    @ApiOperation(value = "Обновить очередь")
    @UrlDeclaration
    @RequestMapping(value = "/{targetId}/request/{requestId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueRequestDto updateRequest(@PathVariable("targetId") Long targetId,
                                         @PathVariable("requestId") Long requestId,
                                         @RequestBody QueueRequestDto queueRequestDto) {
        return queueService.updateQueueRequest(targetId, requestId, queueRequestDto);
    }

    @ApiOperation(value = "Удалить очередь")
    @UrlDeclaration
    @RequestMapping(value = "/{targetId}/request/{requestId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateRequest(@PathVariable("targetId") Long targetId,
                              @PathVariable("requestId") Long requestId) {
        queueService.deleteQueueRequest(targetId, requestId);
    }

    @ApiOperation(value = "Добавить цель")
    @UrlDeclaration(isUser = false)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueTargetDto addQueueTarget(@RequestBody QueueTargetDto queueTargetDto) {
        return queueService.add(queueTargetDto);
    }

    @ApiOperation(value = "Обновить цель")
    @UrlDeclaration(isUser = false)
    @RequestMapping(value = "/{targetId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueueTargetDto updateQueueTarget(@PathVariable("targetId") Long targetId,
                                            @RequestBody QueueTargetDto queueTargetDto) {
        return queueService.update(targetId, queueTargetDto);
    }

    @ApiOperation(value = "Удалить цель")
    @UrlDeclaration(isUser = false)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteQueueTarget(@PathVariable("id") Long id) {
        queueService.delete(id);
    }
}
