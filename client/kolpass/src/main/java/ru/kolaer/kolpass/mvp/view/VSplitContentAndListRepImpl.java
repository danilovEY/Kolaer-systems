package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by danilovey on 16.02.2017.
 */
public class VSplitContentAndListRepImpl implements VSplitContentAndListRep {
    private final BorderPane mainPane;
    private final SplitPane listContentPane;

    public VSplitContentAndListRepImpl() {
        this.listContentPane = new SplitPane();
        this.listContentPane.setOrientation(Orientation.HORIZONTAL);
        this.listContentPane.getItems().addAll(null, null);
        this.mainPane = new BorderPane(this.listContentPane);
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
    public void setEmployeeList(VEmployeeRepositoryList view) {
        this.listContentPane.getItems().set(0, view.getContent());
    }

    @Override
    public void setContent(VRepositoryContent view) {
        this.listContentPane.getItems().set(1, view.getContent());
    }
}
