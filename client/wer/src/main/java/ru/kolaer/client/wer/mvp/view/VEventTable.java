package ru.kolaer.client.wer.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.wer.mvp.model.Event;

import java.util.List;
import java.util.function.Function;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface VEventTable extends BaseView<BorderPane> {
    void setOnSelectEvent(Function<Event, Void> function);

    void setEvents(List<Event> eventList);

    void addEvents(Event event);

    void clear();
}
