package ru.kolaer.api.system.impl;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.system.ui.NotifiAction;
import ru.kolaer.api.system.ui.NotificationUS;

import java.util.Optional;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultNotificationUS implements NotificationUS {

    @Override
    public void showParentNotifi(Parent pane) {
        log.info("Добавление панели в нотификацию");
    }

    @Override
    public void removeParentNotifi(Parent content) {
        log.info("Удалении панели из нотификации");
    }

    @Override
    public void showSimpleNotifi(String title, String text) {
        log.info("Добавление простой нотификации с текстом: {}", text);
    }

    @Override
    public void showErrorNotifi(String title, String text) {
        log.info("Добавление ошибки нотификации с текстом: {}", text);
    }

    @Override
    public void showWarningNotifi(String title, String text) {
        log.info("Добавление предупреждения нотификации с текстом: {}", text);
    }

    @Override
    public void showInformationNotifi(String title, String text) {
        log.info("Добавление информативной нотификации с текстом: {}", text);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration) {
        log.info("Добавление информативной нотификации с текстом и задержкой: {}", text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration) {
        log.info("Добавление простой нотификации с текстом и задержкой: {}", text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions) {
        log.info("Добавление простой нотификации с текстом, задержкой, позиции и действием: {}", text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration, NotifiAction... actions) {
        log.info("Добавление простой нотификации с текстом, задержкой и действием: {}", text);
    }

    @Override
    public void showErrorNotifi(String title, String text, NotifiAction... actions) {
        log.info("Добавление ошибки нотификации с текстом действием: {}", text);
    }

    @Override
    public void showWarningNotifi(String title, String text, NotifiAction... actions) {
        log.info("Добавление предупреждение нотификации с текстом и действием: {}", text);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions) {
        log.info("Добавление информативной нотификации с текстом, задержкой, позицией и действием: {}", text);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration, NotifiAction... actions) {
        log.info("Добавление информативной нотификации с текстом, задержкой и действием: {}", text);
    }

    @Override
    public void showInformationNotifiAdmin(String title, String text, NotifiAction... actions) {
        log.info("Добавление информативной нотификации с текстом, и действием для админа: {}", text);
    }

    @Override
    public void showWarningNotifiAdmin(String title, String text, NotifiAction... actions) {
        log.info("Добавление предупреждение нотификации с текстом, и действием для админа: {}", text);
    }
}
