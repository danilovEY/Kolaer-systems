package ru.kolaer.client.wer.mvp.presenter;

import ru.kolaer.client.wer.mvp.view.VSplitTableDetailedEvent;
import ru.kolaer.common.mvp.presenter.BasePresenter;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface PSplitTableDetailedEvent extends BasePresenter<VSplitTableDetailedEvent> {
    void setEventTable(PEventTable eventTable);
    void setDetailedEvent(PDetailedEvent detailedEvent);
}
