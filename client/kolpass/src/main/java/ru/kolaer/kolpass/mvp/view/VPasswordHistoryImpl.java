package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;

import java.awt.Toolkit;
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
        this.passwordField.setPrefWidth(300);

        this.loginField = new TextField();
        this.loginField.setPrefWidth(300);

        this.labelDateWrite = new Label("Дата записи");

        final Button copyPass = new Button("Копировать");
        copyPass.setOnAction(e -> this.writeToClipboard(this.passwordField.getText()));

        final Button copyLogin = new Button("Копировать");
        copyLogin.setOnAction(e -> this.writeToClipboard(this.loginField.getText()));

        FlowPane flowPane = new FlowPane(5, 5, this.loginField, copyLogin);
        flowPane.setAlignment(Pos.CENTER);
        FlowPane flowPane1 = new FlowPane(5, 5, this.passwordField, copyPass);
        flowPane1.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(5, flowPane, flowPane1);
        vBox.setAlignment(Pos.CENTER);
        //vBox.setStyle("-fx-background-color: red");

        this.mainPane = new BorderPane(vBox);
        this.mainPane.setTop(this.labelDateWrite);
        this.mainPane.setPadding(new Insets(10));

        BorderPane.setAlignment(this.labelDateWrite, Pos.CENTER);
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
    public void setEditable(boolean edit) {
        this.loginField.setEditable(edit);
        this.loginField.setDisable(!edit);
        this.passwordField.setEditable(edit);
        this.passwordField.setDisable(!edit);
    }

    @Override
    public boolean isEditable() {
        return this.loginField.isEditable();
    }

    @Override
    public boolean isChangeData(RepositoryPasswordHistory passwordHistory) {
        return !this.passwordField.getText().equals(passwordHistory.getPassword())
                && !this.loginField.getText().equals(passwordHistory.getLogin());
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
