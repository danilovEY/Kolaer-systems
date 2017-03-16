package ru.kolaer.client.wer.mvp.presenter;

import ru.kolaer.client.wer.mvp.model.MWindowsEvent;
import ru.kolaer.client.wer.mvp.view.VEventTable;
import ru.kolaer.client.wer.mvp.view.VEventTableImpl;

/**
 * Created by danilovey on 16.03.2017.
 */
public class PEventTableImpl implements PEventTable {
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
}
