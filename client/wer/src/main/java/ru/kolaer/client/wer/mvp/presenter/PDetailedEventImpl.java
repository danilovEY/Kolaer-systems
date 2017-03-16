package ru.kolaer.client.wer.mvp.presenter;

import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.EventData;
import ru.kolaer.client.wer.mvp.view.VDetailedEvent;
import ru.kolaer.client.wer.mvp.view.VDetailedEventImpl;

import java.util.List;

/**
 * Created by danilovey on 16.03.2017.
 */
public class PDetailedEventImpl implements PDetailedEvent {
    private VDetailedEvent view;
    private Event event;

    public PDetailedEventImpl() {
        this.view = new VDetailedEventImpl();
    }

    @Override
    public Event getModel() {
        return this.event;
    }

    @Override
    public void setModel(Event model) {
        this.event = model;
    }

    @Override
    public VDetailedEvent getView() {
        return this.view;
    }

    @Override
    public void setView(VDetailedEvent view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.view.setEvent(this.event);
    }
}
