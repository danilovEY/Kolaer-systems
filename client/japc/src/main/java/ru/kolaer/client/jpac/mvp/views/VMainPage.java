package ru.kolaer.client.jpac.mvp.views;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.InitializationView;
import ru.kolaer.api.mvp.view.VComponentUI;

/**
 * Created by danilovey on 06.09.2016.
 */
public class VMainPage implements VComponentUI, InitializationView {
    private BorderPane mainPane;

    @Override
    public void initView() {
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
