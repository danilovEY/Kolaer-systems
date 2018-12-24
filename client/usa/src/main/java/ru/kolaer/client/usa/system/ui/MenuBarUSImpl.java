package ru.kolaer.client.usa.system.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.observers.AuthenticationObserver;
import ru.kolaer.common.system.Authentication;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.system.ui.MenuBarUS;
import ru.kolaer.common.tools.Tools;

import java.util.function.Consumer;

/**
 * Created by danilovey on 09.02.2017.
 */
public class MenuBarUSImpl implements MenuBarUS, AuthenticationObserver {
    private MenuItem authorizationItem;
    private MenuBar menuBar;

    @Override
    public void addMenu(Menu menu) {
        Tools.runOnWithOutThreadFX(() -> this.menuBar.getMenus().add(menu));
    }

    @Override
    public void removeMenu(Menu menu) {
        Tools.runOnWithOutThreadFX(() -> this.menuBar.getMenus().remove(menu));
    }

    @Override
    public void login(AccountDto account) {
        authorizationItem.setText("Выход (" + account.getUsername() + ")");
    }

    @Override
    public void logout(AccountDto account) {
        authorizationItem.setText("Авторизоваться");

        UniformSystemEditorKitSingleton.getInstance().getUISystemUS()
                .getNotification().showInformationNotify("Выход из системы",
                "Выход из системы прошел успешно");
    }

    @Override
    public void initView(Consumer<MenuBarUS> viewVisit) {
        menuBar = new MenuBar();

        authorizationItem = new MenuItem("Авторизоваться");
        authorizationItem.setOnAction(e -> {
            Authentication authentication = UniformSystemEditorKitSingleton.getInstance().getAuthentication();
            if(!authentication.isAuthentication()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS().getDialog().createAndShowLoginToSystemDialog();
            } else {
                authentication.logout();
            }
        });


        Menu fileMenu = new Menu("Файл");
        fileMenu.getItems().add(authorizationItem);
        menuBar.getMenus().add(fileMenu);

        viewVisit.accept(this);
    }

    @Override
    public MenuBar getContent() {
        return menuBar;
    }
}
