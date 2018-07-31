package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.QueueRequestDto;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.QueueService;

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

    @ApiOperation(value = "Получить все очереди у цели")
    @UrlDeclaration
    @RequestMapping(value = "/{id}/request", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<QueueRequestDto> getAll(@PathVariable("id") Long id,
                                        @RequestParam(value = "page", defaultValue = "1") Integer number,
                                        @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return queueService.getAllQueueRequestByTarget(id, number, pageSize);
    }
}
