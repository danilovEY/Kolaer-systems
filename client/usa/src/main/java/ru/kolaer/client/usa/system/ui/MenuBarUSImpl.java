package ru.kolaer.client.usa.system.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.ui.MenuBarUS;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

/**
 * Created by danilovey on 09.02.2017.
 */
public class MenuBarUSImpl implements MenuBarUS, AuthenticationObserver {
    private final MenuItem authorizationItem;
    private final Menu fileMenu;
    private final MenuBar menuBar;

    public MenuBarUSImpl(MenuBar menuBar) {
        this.menuBar = menuBar;
        this.fileMenu = new Menu("Файл");
        this.authorizationItem = new MenuItem("Авторизоваться");
        this.initView();
    }

    private void initView() {
        authorizationItem.setOnAction(e -> {
            final Authentication authentication = UniformSystemEditorKitSingleton.getInstance().getAuthentication();
            if(!authentication.isAuthentication()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS().getDialog().createAndShowLoginToSystemDialog();
            } else {
                authentication.logout();
            }
        });


        fileMenu.getItems().add(authorizationItem);

        this.menuBar.getMenus().add(fileMenu);
    }

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
    public void logout(AccountEntity account) {
        authorizationItem.setText("Авторизоваться");

        UniformSystemEditorKitSingleton.getInstance().getUISystemUS()
                .getNotification().showInformationNotifi("Выход из системы",
                "Выход из системы прошел успешно");
    }
}
