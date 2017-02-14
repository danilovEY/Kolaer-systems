package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryPaneImpl implements VRepositoryPane {
    private BorderPane mainPane;
    private FlowPane contentPane;

    public VRepositoryPaneImpl() {
        this.contentPane = new FlowPane(10, 10);
        this.contentPane.setAlignment(Pos.TOP_CENTER);
        this.contentPane.setPadding(new Insets(10));
        this.contentPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/background.jpg").toString() + "')");

        final ScrollPane scrollPane = new ScrollPane(this.contentPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        this.mainPane = new BorderPane(scrollPane);
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

    @Override
    public void clear() {
        this.contentPane.getChildren().clear();
    }
}
