package ru.kolaer.client.psr.mvp.view.impl;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.psr.mvp.view.VMainPane;

/**
 * Created by danilovey on 01.08.2016.
 */
public class VMainPaneImpl implements VMainPane {
    private BorderPane mainPane;

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
}
