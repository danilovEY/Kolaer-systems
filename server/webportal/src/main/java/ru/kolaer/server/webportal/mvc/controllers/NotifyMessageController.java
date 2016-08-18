package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.NotifyMessageService;

/**
 * Created by danilovey on 18.08.2016.
 */
@RestController
@RequestMapping("/notify")
public class NotifyMessageController {

    @Autowired
    private NotifyMessageService notifyMessageService;

    @UrlDeclaration(description = "Получить последнее оповещение.", isAccessAll = true)
    @RequestMapping(value = "/get/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public NotifyMessage getLastNotifyMessage() {
        return this.notifyMessageService.getLastNotifyMessage();
    }

    @UrlDeclaration(description = "Добавить оповещение.")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addNotifyMessage(@RequestBody NotifyMessage notifyMessage) {
        this.notifyMessageService.add(notifyMessage);
    }

}
