package ru.kolaer.client.wer.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.MWindowsEvent;
import ru.kolaer.client.wer.mvp.view.VEventTable;
import ru.kolaer.client.wer.mvp.view.VEventTableImpl;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 16.03.2017.
 */
public class PEventTableImpl implements PEventTable {
    private final Logger log = LoggerFactory.getLogger(PEventTableImpl.class);
    private final UniformSystemEditorKit editorKit;
    private Integer lastLoadEventId;
    private VEventTable view;
    private MWindowsEvent model;

    public PEventTableImpl(UniformSystemEditorKit editorKit) {
        this(editorKit, MWindowsEvent.EMPTY);
    }

    public PEventTableImpl(UniformSystemEditorKit editorKit, MWindowsEvent model) {
        this.view = new VEventTableImpl();
        this.editorKit = editorKit;

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

        events.stream()
                .filter(event -> event.getSystem().getKeyword().equals("0x8010000000000000"))
                .findFirst()
                .ifPresent(event -> this.editorKit.getUISystemUS()
                            .getPopupNotification()
                            .showWarningNotify("Отказ в доступе!",new SimpleDateFormat("dd.MM.yyyy HH:mm.ss")
                                    .format(event.getSystem().getTimeCreated().getSystemTime()) + " - \""
                                    + event.getSystem().getComputer() + "\"")
                );

        this.view.addEvents(events);

        this.view.sortByDate(Comparator
                .comparing((Event e) -> e.getSystem().getTimeCreated().getSystemTime())
                .reversed()
        );
    }
}
