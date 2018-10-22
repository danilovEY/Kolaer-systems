package ru.kolaer.client.wer.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface VSplitTableDetailedEvent extends BaseView<VSplitTableDetailedEvent, BorderPane> {
    void setEventTable(VEventTable view);
    void setDetailedEvent(VDetailedEvent view);
}
