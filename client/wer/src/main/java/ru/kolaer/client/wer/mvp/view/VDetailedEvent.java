package ru.kolaer.client.wer.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface VDetailedEvent extends BaseView<VDetailedEvent, BorderPane> {
    void setEvent(Event event);
}
