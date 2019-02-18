package ru.kolaer.client.core.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.core.system.ui.StaticUS;
import ru.kolaer.client.core.system.ui.StaticView;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultStaticUS implements StaticUS {

    @Override
    public void addStaticView(StaticView staticView) {
        log.info("Добавлена представление в статическую зону");
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        log.info("Удалено представление из статической зоны");
    }
}
