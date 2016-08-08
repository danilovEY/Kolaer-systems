package ru.kolaer.client.psr.mvp.view.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.view.VMainPane;

/**
 * Created by danilovey on 01.08.2016.
 */
public class VMainPaneImpl implements VMainPane {
    private BorderPane mainPane;
    private MenuItem loginMenu;
    private MenuItem createPsrProjectMenu;
    private boolean isInit = false;

    public VMainPaneImpl() {
        this.mainPane = new BorderPane();
    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void initializationView() {
        final MenuBar menuBar = new MenuBar();

        final Menu fileMenu = new Menu("Файл");
        final Menu psrMenu = new Menu("ПСР");

        this.createPsrProjectMenu = new MenuItem("Создать ПСР проект");

        this.loginMenu = new MenuItem("Вход");

        fileMenu.getItems().addAll(this.loginMenu);
        psrMenu.getItems().addAll(this.createPsrProjectMenu);

        menuBar.getMenus().addAll(fileMenu, psrMenu);

        this.mainPane.setTop(menuBar);

        this.isInit = true;
    }

    @Override
    public boolean isInitializationView() {
        return this.isInit;
    }

    @Override
    public void loginAction(final EventHandler<ActionEvent> event) {
        this.loginMenu.setOnAction(event);
    }

    @Override
    public EventHandler<ActionEvent> getLoginAction() {
        return this.loginMenu.getOnAction();
    }

    @Override
    public void logoutAction(EventHandler<ActionEvent> event) {
        this.loginMenu.setOnAction(event);
    }

    @Override
    public void createPsrAction(EventHandler<ActionEvent> event) {
        this.createPsrProjectMenu.setOnAction(event);
    }

    @Override
    public void setUserName(String userName) {
        if(userName != null)
            Tools.runOnThreadFX(() -> this.loginMenu.setText(userName));
    }

    @Override
    public void setEnableCreatePstMenuItem(boolean enable) {
        this.createPsrProjectMenu.setDisable(enable);
    }

}
