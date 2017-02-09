package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryPaneImpl implements VRepositoryPane {
    private BorderPane mainPane;
    private FlowPane contentPane;

    public VRepositoryPaneImpl() {
        this.contentPane = new FlowPane();
        this.mainPane = new BorderPane(this.contentPane);
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
    public void addRepository(VRepositoryPane vRepositoryPane) {
        this.contentPane.getChildren().add(vRepositoryPane.getContent());
    }

    @Override
    public void removeRepository(VRepositoryPane vRepositoryPane) {
        this.contentPane.getChildren().remove(vRepositoryPane.getContent());
    }
}
