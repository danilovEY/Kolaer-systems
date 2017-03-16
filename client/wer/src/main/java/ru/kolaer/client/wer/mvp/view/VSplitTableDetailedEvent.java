package ru.kolaer.client.wer.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface VSplitTableDetailedEvent extends BaseView<BorderPane> {
    void setEventTable(VEventTable view);
    void setDetailedEvent(VDetailedEvent view);
}
