package ru.kolaer.api.system.impl;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.system.ui.MenuBarUS;

import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultMenuBarUS implements MenuBarUS {

    @Override
    public void addMenu(Menu menu) {
        log.info("Добалено меню");
    }

    @Override
    public void removeMenu(Menu menu) {
        log.info("Удалено меню");
    }

    @Override
    public void initView(Consumer<MenuBarUS> viewVisit) {

    }

    @Override
    public MenuBar getContent() {
        return null;
    }
}
