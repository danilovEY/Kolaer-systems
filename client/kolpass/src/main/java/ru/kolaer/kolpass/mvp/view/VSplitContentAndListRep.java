package ru.kolaer.kolpass.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 16.02.2017.
 */
public interface VSplitContentAndListRep extends BaseView<BorderPane> {
    void setEmployeeList(VEmployeeRepositoryList view);
    void setContent(VRepositoryContent view);
}
