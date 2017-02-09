package ru.kolaer.kolpass.mvp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VPasswordHistoryImpl implements VPasswordHistory {
    private final BorderPane mainPane;
    private final TextField passwordField;
    private final TextField loginField;
    private final Label labelDateWrite;

    public VPasswordHistoryImpl() {
        this.passwordField = new TextField();
        this.loginField = new TextField();
        this.labelDateWrite = new Label("Дата записи");

        final Button copyPass = new Button("Копировать");
        copyPass.setOnAction(e -> this.writeToClipboard(this.passwordField.getText()));

        this.mainPane = new BorderPane(new HBox(this.labelDateWrite, this.labelDateWrite, this.passwordField, copyPass));
    }


    @Override
    public String getDate() {
        return this.labelDateWrite.getText();
    }

    @Override
    public void setDate(String date) {
        this.labelDateWrite.setText(date);
    }

    @Override
    public String getLogin() {
        return this.loginField.getText();
    }

    @Override
    public void setLogin(String login) {
        this.loginField.setText(login);
    }

    @Override
    public String getPassword() {
        return this.passwordField.getText();
    }

    @Override
    public void setPassword(String name) {
        this.passwordField.setText(name);
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    private void writeToClipboard(String s) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, null);
    }
}
