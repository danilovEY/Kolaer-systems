package ru.kolaer.client.wer.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.common.mvp.view.BaseView;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface VEventTable extends BaseView<VEventTable, BorderPane> {
    void setOnSelectEvent(Function<Event, Void> function);

    void addEvents(List<Event> eventList);

    void sortByDate(Comparator<Event> comparator);

    void addEvents(Event event);

    void clear();
}
