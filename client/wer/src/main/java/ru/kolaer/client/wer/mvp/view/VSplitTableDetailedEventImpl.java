package ru.kolaer.client.wer.mvp.view;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by danilovey on 16.03.2017.
 */
public class VSplitTableDetailedEventImpl implements VSplitTableDetailedEvent {
    private final SplitPane splitPane;
    private BorderPane mainPane;

    public VSplitTableDetailedEventImpl() {
        this.mainPane = new BorderPane();
        this.splitPane = new SplitPane();

        this.init();
    }

    private void init() {
        this.splitPane.setOrientation(Orientation.VERTICAL);
        this.splitPane.getItems().setAll(null, null);

        this.mainPane.setCenter(this.splitPane);
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
    public void setEventTable(VEventTable view) {
        this.splitPane.getItems().set(0, view.getContent());
    }

    @Override
    public void setDetailedEvent(VDetailedEvent view) {
        this.splitPane.getItems().set(1, view.getContent());
    }
}
