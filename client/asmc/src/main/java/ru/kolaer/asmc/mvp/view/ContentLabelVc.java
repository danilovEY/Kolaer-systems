package ru.kolaer.asmc.mvp.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.mvp.model.DataServiceObserver;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.presenter.Access;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface ContentLabelVc extends BaseView<ContentLabelVc, BorderPane>, Access, DataServiceObserver {
    void setSelectedGroup(MGroup mGroup);
}
