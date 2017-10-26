package ru.kolaer.api.system.impl;

import javafx.geometry.Pos;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.system.ui.NotifyAction;

import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultNotificationUS implements NotificationUS {

    @Override
    public void showSimpleNotify(String title, String text) {
        log.info("Добавление простой нотификации с текстом: {}", text);
    }

    @Override
    public void showErrorNotify(String title, String text) {
        log.info("Добавление ошибки нотификации с текстом: {}", text);
    }

    @Override
    public void showWarningNotify(String title, String text) {
        log.info("Добавление предупреждения нотификации с текстом: {}", text);
    }

    @Override
    public void showInformationNotify(String title, String text) {
        log.info("Добавление информативной нотификации с текстом: {}", text);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration) {
        log.info("Добавление информативной нотификации с текстом и задержкой: {}", text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration) {
        log.info("Добавление простой нотификации с текстом и задержкой: {}", text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        log.info("Добавление простой нотификации с текстом, задержкой, позиции и действием: {}", text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        log.info("Добавление простой нотификации с текстом, задержкой и действием: {}", text);
    }

    @Override
    public void showErrorNotify(String title, String text, List<NotifyAction> actions) {
        log.info("Добавление ошибки нотификации с текстом действием: {}", text);
    }

    @Override
    public void showWarningNotify(String title, String text, List<NotifyAction> actions) {
        log.info("Добавление предупреждение нотификации с текстом и действием: {}", text);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        log.info("Добавление информативной нотификации с текстом, задержкой, позицией и действием: {}", text);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        log.info("Добавление информативной нотификации с текстом, задержкой и действием: {}", text);
    }

    @Override
    public void showErrorNotify(ServerExceptionMessage exceptionMessage) {
        log.info("Ошибка: {}", exceptionMessage);
    }
}
