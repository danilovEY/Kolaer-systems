package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.NotifyMessageService;

/**
 * Created by danilovey on 18.08.2016.
 */
@RestController
@RequestMapping("/non-security/notify")
@Api(tags = "Нотификация", description = "Пуш уведомления")
public class NotifyMessageController {

    private final NotifyMessageService notifyMessageService;

    @Autowired
    public NotifyMessageController(NotifyMessageService notifyMessageService) {
        this.notifyMessageService = notifyMessageService;
    }

    @ApiOperation(
            value = "Получить последнее оповещение"
    )
    @UrlDeclaration(description = "Получить последнее оповещение", isAccessAll = true)
    @RequestMapping(value = "/get/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public NotifyMessageDto getLastNotifyMessage() {
        return notifyMessageService.getLastNotifyMessage();
    }

    @ApiOperation(
            value = "Добавить оповещение"
    )
    @UrlDeclaration(description = "Добавить оповещение", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addNotifyMessage(@ApiParam(value = "Сообщение", required = true) @RequestBody NotifyMessageDto notifyMessage) {
        notifyMessage.setId(1L);
        this.notifyMessageService.save(notifyMessage);
    }

}
