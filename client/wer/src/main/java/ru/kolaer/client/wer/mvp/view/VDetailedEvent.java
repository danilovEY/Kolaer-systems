package ru.kolaer.client.wer.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.wer.mvp.model.Event;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface VDetailedEvent extends BaseView<BorderPane> {
    void setEvent(Event event);
}
