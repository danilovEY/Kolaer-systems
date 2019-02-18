package ru.kolaer.client.core.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.core.system.ui.ProgressBarObservable;
import ru.kolaer.client.core.system.ui.StatusBarUS;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultStatusBarUS implements StatusBarUS {
    @Override
    public void addProgressBar(ProgressBarObservable progressBar) {
        log.info("Добавлен слушатель прогресс бара");
    }

    @Override
    public void addMessage(String message) {
        log.info("Добавлено сообщение: {}", message);
    }
}
