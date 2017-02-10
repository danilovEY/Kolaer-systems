package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryPaneImpl implements VRepositoryPane {
    private BorderPane mainPane;
    private FlowPane contentPane;

    public VRepositoryPaneImpl() {
        this.contentPane = new FlowPane(5, 5);
        this.mainPane = new BorderPane(this.contentPane);
        this.mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/background.jpg").toString() + "')");
        this.mainPane.setPadding(new Insets(10));
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void addRepositoryPassword(VRepositoryPassword vRepositoryPassword) {
        this.contentPane.getChildren().add(vRepositoryPassword.getContent());
    }

    @Override
    public void removeRepositoryPassword(VRepositoryPassword vRepositoryPassword) {
        this.contentPane.getChildren().remove(vRepositoryPassword.getContent());
    }
}
