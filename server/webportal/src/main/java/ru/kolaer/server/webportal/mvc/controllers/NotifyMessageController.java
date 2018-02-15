package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
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
            value = "Получить последнее оповещение"
    )
    @UrlDeclaration(description = "Получить оповещения", isAccessAll = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<NotifyMessageDto> getNotifyMessages(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                    @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return notifyMessageService.getAll(number, pageSize);
    }

}
