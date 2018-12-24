package ru.kolaer.client.wer.mvp.presenter;

import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.MWindowsEvent;
import ru.kolaer.client.wer.mvp.model.ObserverEventLoader;
import ru.kolaer.client.wer.mvp.view.VEventTable;
import ru.kolaer.common.dto.BaseModel;
import ru.kolaer.common.mvp.presenter.BasePresenter;

import java.util.function.Function;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface PEventTable extends BaseModel<MWindowsEvent>, BasePresenter<VEventTable>, ObserverEventLoader {
    void setOnSelectEvent(Function<Event, Void> function);
}
