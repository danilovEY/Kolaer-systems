package ru.kolaer.client.core.system.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import ru.kolaer.client.core.mvp.view.BaseView;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface MenuBarUS extends BaseView<MenuBarUS, MenuBar> {
    void addMenu(Menu menu);
    void removeMenu(Menu menu);
}
