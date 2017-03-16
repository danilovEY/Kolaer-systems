package ru.kolaer.client.wer.mvp.presenter;

import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.view.VSplitTableDetailedEvent;
import ru.kolaer.client.wer.mvp.view.VSplitTableDetailedEventImpl;

/**
 * Created by danilovey on 16.03.2017.
 */
public class PSplitTableDetailedEventImpl implements PSplitTableDetailedEvent {
    private VSplitTableDetailedEvent view;
    private PEventTable eventTable;
    private PDetailedEvent detailedEvent;

    public PSplitTableDetailedEventImpl() {
        this.view = new VSplitTableDetailedEventImpl();
    }

    @Override
    public VSplitTableDetailedEvent getView() {
        return this.view;
    }

    @Override
    public void setView(VSplitTableDetailedEvent view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.view.setEventTable(eventTable.getView());
        this.view.setDetailedEvent(detailedEvent.getView());

        this.eventTable.setOnSelectEvent(event -> {
            this.detailedEvent.setModel(event);
            this.detailedEvent.updateView();
            return null;
        });
    }

    @Override
    public void setEventTable(PEventTable eventTable) {
        this.eventTable = eventTable;

    }

    @Override
    public void setDetailedEvent(PDetailedEvent detailedEvent) {
        this.detailedEvent = detailedEvent;
    }
}
