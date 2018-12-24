package ru.kolaer.client.wer.mvp.presenter;

import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.view.VDetailedEvent;
import ru.kolaer.common.dto.BaseModel;
import ru.kolaer.common.mvp.presenter.BasePresenter;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface PDetailedEvent extends BasePresenter<VDetailedEvent>, BaseModel<Event> {
}
