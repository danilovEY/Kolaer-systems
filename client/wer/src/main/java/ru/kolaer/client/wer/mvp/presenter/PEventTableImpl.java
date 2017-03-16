package ru.kolaer.client.wer.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.MWindowsEvent;
import ru.kolaer.client.wer.mvp.view.VEventTable;
import ru.kolaer.client.wer.mvp.view.VEventTableImpl;

import java.util.function.Function;

/**
 * Created by danilovey on 16.03.2017.
 */
public class PEventTableImpl implements PEventTable {
    private final Logger log = LoggerFactory.getLogger(PEventTableImpl.class);

    private VEventTable view;
    private MWindowsEvent model;

    public PEventTableImpl() {
        this(MWindowsEvent.EMPTY);
    }

    public PEventTableImpl(MWindowsEvent model) {
        this.view = new VEventTableImpl();
        this.model = model == null ? MWindowsEvent.EMPTY : model;
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
        this.view.setEvents(this.model.loadAllWindowsEvent());
    }

    @Override
    public void setOnSelectEvent(Function<Event, Void> function) {
        this.view.setOnSelectEvent(function);
    }
}
