package ru.kolaer.client.wer.mvp.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.model.Event;

/**
 * Created by danilovey on 16.03.2017.
 */
public class VDetailedEventImpl implements VDetailedEvent {
    private BorderPane mainPane;

    public VDetailedEventImpl() {
        this.mainPane = new BorderPane();
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
    public void setEvent(Event event) {
        this.mainPane.setCenter(new Label(event.getSystem().getComputer()));
    }
}
