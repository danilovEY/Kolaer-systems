package ru.kolaer.kolpass.mvp.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryPasswordImpl implements VRepositoryPassword {
    private HBox content;
    private BorderPane mainPane;
    private Label labelName;

    public VRepositoryPasswordImpl() {
        this.content = new HBox();
        this.mainPane = new BorderPane(this.content);
        this.labelName = new Label();

        this.mainPane.setTop(this.labelName);
    }

    @Override
    public void setName(String name) {
        this.labelName.setText(name);
    }

    @Override
    public String getName() {
        return this.labelName.getText();
    }

    @Override
    public void setLastPassword(VPasswordHistory password) {
        this.content.getChildren().set(0, password.getContent());
    }

    @Override
    public void setFirstPassword(VPasswordHistory password) {
        this.content.getChildren().set(2, password.getContent());
    }

    @Override
    public void setPrevPassword(VPasswordHistory password) {
        this.content.getChildren().set(1, password.getContent());
    }

    @Override
    public VPasswordHistory getLastPassword() {
        return null;
    }

    @Override
    public VPasswordHistory getFirstPassword() {
        return null;
    }

    @Override
    public VPasswordHistory getPrevPassword() {
        return null;
    }

    @Override
    public void addPasswordHistory(VPasswordHistory passwordHistory) {

    }

    @Override
    public void removePasswordHistory(VPasswordHistory passwordHistory) {

    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }
}
