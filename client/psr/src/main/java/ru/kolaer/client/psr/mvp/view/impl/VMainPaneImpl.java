package ru.kolaer.client.psr.mvp.view.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.psr.mvp.view.VMainPane;

/**
 * Created by danilovey on 01.08.2016.
 */
public class VMainPaneImpl implements VMainPane {
    private BorderPane mainPane;
    private MenuItem loginMenu;
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
        this.mainPane = new BorderPane();
        final MenuBar menuBar = new MenuBar();

        final Menu fileMenu = new Menu("Файл");
        final Menu psrMenu = new Menu("ПСР");

        this.loginMenu = new MenuItem("Вход");

        fileMenu.getItems().addAll(this.loginMenu);
        menuBar.getMenus().addAll(fileMenu, psrMenu);

        this.mainPane.setTop(menuBar);
    }

    @Override
    public void loginAction(final EventHandler<ActionEvent> event) {
        this.loginMenu.setOnAction(event);
    }

    @Override
    public void logoutAction(EventHandler<ActionEvent> event) {
        this.loginMenu.setOnAction(event);
    }

    @Override
    public void setUserName(String userName) {
        this.loginMenu.setText(userName);
    }

}
