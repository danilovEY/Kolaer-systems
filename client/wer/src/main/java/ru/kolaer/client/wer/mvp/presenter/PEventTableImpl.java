package ru.kolaer.client.wer.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.MWindowsEvent;
import ru.kolaer.client.wer.mvp.view.VEventTable;
import ru.kolaer.client.wer.mvp.view.VEventTableImpl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 16.03.2017.
 */
public class PEventTableImpl implements PEventTable {
    private final Logger log = LoggerFactory.getLogger(PEventTableImpl.class);
    private Integer lastLoadEventId;
    private VEventTable view;
    private MWindowsEvent model;

    public PEventTableImpl() {
        this(MWindowsEvent.EMPTY);
    }

    public PEventTableImpl(MWindowsEvent model) {
        this.view = new VEventTableImpl();

        this.lastLoadEventId = 0;

        this.model = model == null ? MWindowsEvent.EMPTY : model;
        this.model.registerObserver(this);
    }

    @Override
    public MWindowsEvent getModel() {
        return this.model;
    }

    @Override
    public void setModel(MWindowsEvent model) {
        this.model = model;
    }

    @Override
    public VEventTable getView() {
        return this.view;
    }

    @Override
    public void setView(VEventTable view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.updateEvent(this.model.loadAllWindowsEvent());
    }

    @Override
    public void setOnSelectEvent(Function<Event, Void> function) {
        this.view.setOnSelectEvent(function);
    }

    @Override
    public void updateEvent(List<Event> eventList) {
        List<Event> events = eventList.stream()
                .filter(event -> event.getSystem().getEventRecordId() > this.lastLoadEventId)
                .collect(Collectors.toList());

        if(!events.isEmpty()) {
            events.stream().findFirst().ifPresent(event ->
                this.lastLoadEventId = event.getSystem().getEventRecordId()
            );
        }

        this.view.addEvents(events);

        this.view.sortByDate(Comparator
                .comparing((Event e) -> e.getSystem().getTimeCreated().getSystemTime())
                .reversed()
        );
    }
}
