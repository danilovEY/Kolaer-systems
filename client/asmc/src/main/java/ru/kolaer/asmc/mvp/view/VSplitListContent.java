package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface VSplitListContent extends BaseView<VSplitListContent, BorderPane> {
    void setVGroupList(VGroupTree vGroupTree);
    void setVContentLabel(VContentLabel vContentLabel);
}
