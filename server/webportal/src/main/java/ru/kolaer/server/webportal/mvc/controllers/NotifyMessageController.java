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
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.entities.other.NotifyMessageDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.NotifyMessageService;

/**
 * Created by danilovey on 18.08.2016.
 */
@RestController
@RequestMapping("/non-security/notify")
@Api(tags = "Нотификация", description = "Пуш уведомления")
public class NotifyMessageController extends BaseController {

    @Autowired
    private NotifyMessageService notifyMessageService;

    @ApiOperation(
            value = "Получить последнее оповещение",
            notes = "Получить последнее оповещение"
    )
    @UrlDeclaration(description = "Получить последнее оповещение.", isAccessAll = true)
    @RequestMapping(value = "/get/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public NotifyMessage getLastNotifyMessage() {
        return this.notifyMessageService.getLastNotifyMessage();
    }

    @ApiOperation(
            value = "Добавить оповещение",
            notes = "Добавить оповещение"
    )
    @UrlDeclaration(description = "Добавить оповещение.", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addNotifyMessage(@ApiParam(value = "Сообщение", required = true) @RequestBody NotifyMessage notifyMessage) {
        notifyMessage.setId(1);
        this.notifyMessageService.update(new NotifyMessageDecorator(notifyMessage));
    }

}
